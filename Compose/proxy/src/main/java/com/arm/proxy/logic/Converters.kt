package com.arm.proxy.logic

import com.arm.base.model.*
import com.arm.proxy.coroutineScope.ChatCoroutineScope
import com.tencent.imsdk.v2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   :
 *    version: 1.0
 */
internal interface Converters {

    companion object{
        private val chatCoroutineScope:CoroutineScope = ChatCoroutineScope()
    }

    val coroutineScope:CoroutineScope
        get() = chatCoroutineScope

    /**
     * 把在腾讯拿到的信息转为PersonProfile格式
     */
    fun convertPersonProfile(timUserFullInfo: V2TIMUserFullInfo):PersonProfile{
       return PersonProfile(
           userId = timUserFullInfo.userID ?: "",
           nickname = timUserFullInfo.nickName ?: "",
           remark = timUserFullInfo.nickName ?: "",
           faceUrl = timUserFullInfo.faceUrl ?: "",
           signature = timUserFullInfo.selfSignature ?: ""
       )
    }

    /**
     * 好友的信息转换
     */
    fun convertFriendProfile(v2TIMFriendInfo: V2TIMFriendInfo): PersonProfile {
        return PersonProfile(
            userId = v2TIMFriendInfo.userID ?: "",
            nickname = v2TIMFriendInfo.userProfile?.nickName ?: "",
            remark = v2TIMFriendInfo.friendRemark ?: "",
            faceUrl = v2TIMFriendInfo.userProfile?.faceUrl ?: "",
            signature = v2TIMFriendInfo.userProfile?.selfSignature ?: ""
        )
    }

    /**
     * 转换好友资料，加了一个是否是好友的判断
     */
    fun convertFriendProfile(v2TIMFriendInfo: V2TIMFriendInfoResult): PersonProfile {
        return convertFriendProfile(v2TIMFriendInfo.friendInfo).copy(
            isFriend = v2TIMFriendInfo.relation == V2TIMFriendCheckResult.V2TIM_FRIEND_RELATION_TYPE_BOTH_WAY ||
                    v2TIMFriendInfo.relation == V2TIMFriendCheckResult.V2TIM_FRIEND_RELATION_TYPE_IN_MY_FRIEND_LIST
        )
    }

    /**
     * 群组成员信息的转换
     */
    fun convertGroupMember(
        memberFullInfo: V2TIMGroupMemberInfo
    ): GroupMemberProfile {
        val fullInfo = memberFullInfo as? V2TIMGroupMemberFullInfo
        val role = fullInfo?.role ?: -11111
        return GroupMemberProfile(
            userId = memberFullInfo.userID ?: "",
            faceUrl = memberFullInfo.faceUrl ?: "",
            nickname = memberFullInfo.nickName ?: "",
            remark = memberFullInfo.friendRemark ?: "",
            signature = "",
            role = convertRole(role),
            isOwner = role == V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_OWNER,
            joinTime = (memberFullInfo as? V2TIMGroupMemberFullInfo)?.joinTime ?: 0,
        )
    }

    /**
     * 群组中的身份
     */
    private fun convertRole(roleType: Int): String {
        return when (roleType) {
            V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_MEMBER -> {
                "群成员"
            }
            V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_ADMIN -> {
                "群管理员"
            }
            V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_OWNER -> {
                "群主"
            }
            else -> {
                "unknown"
            }
        }
    }

    /**
     * 删除对话
     * suspendCancellableCoroutine 和suspendCoroutine
     * ① 使用 suspendCancellableCoroutine 和 suspendCoroutine 可以将回调函数转换为协程
     * ② SuspendCancellableCoroutine 返回一个 CancellableContinuation，
     * 它可以用 resume、resumeWithException 来处理回调 和抛出 CancellationException 异常。
     * 它与 suspendCoroutine的唯一区别就是 SuspendCancellableCoroutine 可以通过 cancel() 方法手动取消协程的执行，
     * 而 suspendCoroutine 没有该方法。
     * ③ 尽可能使用 suspendCancellableCoroutine 而不是 suspendCoroutine ，因为协程的取消是可控的
     *
     * 这里suspendCancellableCoroutine相当于对之前使用回掉的写法的协程的兼容，通过resume来返回协程
     */
    suspend fun deleteConversation(id: String): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getConversationManager().deleteConversation(
                id, object : V2TIMCallback {
                    override fun onSuccess() {
                        continuation.resume(ActionResult.Success)
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(ActionResult.Failed(desc ?: ""))
                    }
                })
        }
    }

    /**
     * 删除单聊的会话
     */
    suspend fun deleteC2CConversation(userId: String): ActionResult {
        return deleteConversation(String.format("c2c_%s", userId))
    }

    /**
     * 删除群聊
     */
    suspend fun deleteGroupConversation(groupId: String): ActionResult {
        return deleteConversation(String.format("group_%s", groupId))
    }

    /**
     * 获取会话的id
     */
    fun getConversationId(conversation: Conversation): String {
        return when (conversation) {
            is Conversation.C2CConversation -> {
                String.format("c2c_%s", conversation.id)
            }
            is Conversation.GroupConversation -> {
                String.format("group_%s", conversation.id)
            }
        }
    }

    /**
     * 转化消息列表
     */
    fun convertMessage(messageList: List<V2TIMMessage>?): List<Message> {
        return messageList?.mapNotNull { convertMessage(it) } ?: emptyList()
    }

    /**
     * 转化消息
     */
    fun convertMessage(timMessage: V2TIMMessage?, withGroupTip: Boolean = false): Message? {
        val groupId = timMessage?.groupID
        val userId = timMessage?.userID
        if (groupId.isNullOrBlank() && userId.isNullOrBlank()) {
            return null
        }
        val senderProfile = PersonProfile(
            userId = timMessage.sender,
            faceUrl = timMessage.faceUrl ?: "",
            nickname = timMessage.nickName ?: "",
            remark = timMessage.friendRemark ?: "",
            signature = ""
        )
        return kotlin.run {
            val elementType = timMessage.elemType
            if (elementType == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT) {
                if (timMessage.isSelf) {
                    TextMessage.SelfTextMessage(
                        msgId = timMessage.msgID ?: "",
                        msg = timMessage.textElem?.text ?: "",
                        state = convertMessageState(timMessage.status),
                        timestamp = timMessage.timestamp,
                        sender = senderProfile
                    )
                } else {
                    TextMessage.FriendTextMessage(
                        msgId = timMessage.msgID ?: "",
                        msg = timMessage.textElem?.text ?: "",
                        timestamp = timMessage.timestamp,
                        sender = senderProfile
                    )
                }
            } else if (withGroupTip && elementType == V2TIMMessage.V2TIM_ELEM_TYPE_GROUP_TIPS) {
                TextMessage.FriendTextMessage(
                    msgId = timMessage.msgID ?: "",
                    msg = "[修改了群资料]",
                    timestamp = timMessage.timestamp,
                    sender = senderProfile
                )
            } else {
                null
            }
        }?.apply {
            tag = timMessage
        }
    }

    /**
     * 转化消息状态
     */
    private fun convertMessageState(state: Int): MessageState {
        return when (state) {
            V2TIMMessage.V2TIM_MSG_STATUS_SENDING -> {
                MessageState.Sending
            }
            V2TIMMessage.V2TIM_MSG_STATUS_SEND_SUCC -> {
                MessageState.Completed
            }
            V2TIMMessage.V2TIM_MSG_STATUS_SEND_FAIL -> {
                MessageState.SendFailed
            }
            else -> {
                MessageState.Completed
            }
        }
    }



}