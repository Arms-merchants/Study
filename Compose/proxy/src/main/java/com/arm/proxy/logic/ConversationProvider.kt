package com.arm.proxy.logic

import com.arm.base.model.ActionResult
import com.arm.base.model.Conversation
import com.arm.base.provider.IConversationProvider
import com.tencent.imsdk.v2.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   : Flow需要提供默认值，会话相关的操作
 *    version: 1.0
 */
class ConversationProvider : IConversationProvider, Converters {

    override val conversationList = MutableStateFlow<List<Conversation>>(emptyList())

    override val totalUnreadCount = MutableStateFlow(0L)


    init {
        V2TIMManager.getConversationManager()
            .addConversationListener(object : V2TIMConversationListener() {
                override fun onConversationChanged(conversationList: MutableList<V2TIMConversation>) {
                    getConversationList()
                }

                override fun onNewConversation(conversationList: MutableList<V2TIMConversation>?) {
                    getConversationList()
                }

                override fun onTotalUnreadMessageCountChanged(totalUnreadCount: Long) {
                    this@ConversationProvider.totalUnreadCount.value = totalUnreadCount
                }
            })
    }

    /**
     * 获取会话列表
     */
    override fun getConversationList() {
        coroutineScope.launch {
            dispatchConversationList(getConversationListOrigin())
        }
    }


    override suspend fun pinConversation(conversation: Conversation, pin: Boolean): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getConversationManager()
                .pinConversation(getConversationId(conversation), pin, object : V2TIMCallback {
                    override fun onSuccess() {
                        continuation.resume(ActionResult.Success)
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(
                            ActionResult.Failed(
                                code = code,
                                reason = desc ?: ""
                            )
                        )
                    }
                })
        }
    }

    /**
     * 删除会话
     */
    override suspend fun deleteConversation(conversation: Conversation): ActionResult {
        return deleteConversation(getConversationId(conversation))
    }

    /**
     * 分发会话列表的值
     */
    private fun dispatchConversationList(conversationList: List<Conversation>) {
        this.conversationList.value = conversationList
    }

    /**
     * 获取会话列表的来源
     */
    private suspend fun getConversationListOrigin(): List<Conversation> {
        var nextStep = 0L
        val conversationList = mutableListOf<Conversation>()
        while (true) {
            val pair = getConversationList(nextStep = nextStep)
            conversationList.addAll(pair.first)
            nextStep = pair.second
            if (nextStep <= 0) {
                break
            }
        }
        return conversationList
    }

    /**
     *
     */
    private suspend fun getConversationList(nextStep: Long): Pair<List<Conversation>, Long> {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getConversationManager().getConversationList(nextStep, 100,
                object : V2TIMValueCallback<V2TIMConversationResult> {
                    override fun onSuccess(result: V2TIMConversationResult) {
                        continuation.resume(
                            Pair(
                                convertConversation(result.conversationList),
                                if (result.isFinished) {
                                    -111
                                } else {
                                    result.nextSeq
                                }
                            )
                        )
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(Pair(emptyList(), -111))
                    }
                })
        }
    }

    /**
     * 转化会话列表
     */
    private fun convertConversation(convertersList: List<V2TIMConversation>?): List<Conversation> {
        return convertersList?.mapNotNull { convertConversation(it) }?.sortedByDescending {
            if (it.isPinned) {
                it.lastMessage.timestamp.toDouble() + Long.MAX_VALUE
            } else {
                it.lastMessage.timestamp.toDouble()
            }
        } ?: emptyList()
    }

    /**
     * 转化会话消息
     */
    private fun convertConversation(conversation: V2TIMConversation): Conversation? {
        return when (conversation.type) {
            V2TIMConversation.V2TIM_C2C -> {
                val lastMessage =
                    convertMessage(timMessage = conversation.lastMessage)
                        ?: return null
                return Conversation.C2CConversation(
                    userID = conversation.userID ?: "",
                    name = conversation.showName ?: "",
                    faceUrl = conversation.faceUrl ?: "",
                    unreadMessageCount = conversation.unreadCount,
                    lastMessage = lastMessage,
                    isPinned = conversation.isPinned
                )
            }
            V2TIMConversation.V2TIM_GROUP -> {
                val lastMessage =
                    convertMessage(timMessage = conversation.lastMessage, withGroupTip = true)
                        ?: return null
                return Conversation.GroupConversation(
                    groupId = conversation.groupID ?: "",
                    name = conversation.showName ?: "",
                    faceUrl = conversation.faceUrl ?: "",
                    unreadMessageCount = conversation.unreadCount,
                    lastMessage = lastMessage,
                    isPinned = conversation.isPinned
                )
            }
            else -> {
                null
            }
        }
    }

}