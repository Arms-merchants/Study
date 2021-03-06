package com.arm.proxy.logic

import com.arm.base.model.ActionResult
import com.arm.base.model.GroupMemberProfile
import com.arm.base.model.GroupProfile
import com.arm.base.provider.IGroupProvider
import com.tencent.imsdk.v2.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   :
 *    version: 1.0
 */
class GroupProvider: IGroupProvider, Converters {

    override val joinedGroupList = MutableStateFlow<List<GroupProfile>>(emptyList())

    init {
        V2TIMManager.getInstance().addGroupListener(object : V2TIMGroupListener() {
            override fun onMemberEnter(
                groupID: String,
                memberList: MutableList<V2TIMGroupMemberInfo>
            ) {
                getJoinedGroupList()
            }

            override fun onQuitFromGroup(groupID: String) {
                getJoinedGroupList()
            }

            override fun onGroupInfoChanged(
                groupID: String?,
                changeInfos: MutableList<V2TIMGroupChangeInfo>?
            ) {
                getJoinedGroupList()
            }
        })
    }

    override fun getJoinedGroupList() {
        coroutineScope.launch {
            joinedGroupList.value = getJoinedGroupListOrigin().sortedBy { it.name }
        }
    }

    private suspend fun getJoinedGroupListOrigin(): List<GroupProfile> {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getGroupManager().getJoinedGroupList(object :
                V2TIMValueCallback<List<V2TIMGroupInfo>> {
                override fun onSuccess(t: List<V2TIMGroupInfo>) {
                    continuation.resume(convertGroup(t))
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(emptyList())
                }
            })
        }
    }

    private fun convertGroup(groupProfileList: List<V2TIMGroupInfo>?): List<GroupProfile> {
        return groupProfileList?.mapNotNull { convertGroup(it) } ?: emptyList()
    }


    private fun convertGroup(groupProfile: V2TIMGroupInfo?): GroupProfile? {
        val group = groupProfile ?: return null
        return GroupProfile(
            id = group.groupID ?: "",
            faceUrl = group.faceUrl ?: "",
            name = group.groupName ?: "",
            introduction = group.introduction ?: "",
            createTime = group.createTime,
            memberCount = group.memberCount
        )
    }

    override suspend fun joinGroup(groupId: String): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getInstance().joinGroup(groupId, "", object : V2TIMCallback {
                override fun onSuccess() {
                    continuation.resume(ActionResult.Success)
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(ActionResult.Failed(code = code, reason = desc ?: ""))
                }
            })
        }
    }

    override suspend fun quitGroup(groupId: String): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getInstance().quitGroup(groupId, object : V2TIMCallback {
                override fun onSuccess() {
                    continuation.resume(ActionResult.Success)
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(ActionResult.Failed(code = code, reason = desc ?: ""))
                }
            })
        }
    }

    override suspend fun getGroupInfo(groupId: String): GroupProfile? {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getGroupManager().getGroupsInfo(listOf(groupId), object :
                V2TIMValueCallback<List<V2TIMGroupInfoResult>> {
                override fun onSuccess(t: List<V2TIMGroupInfoResult>) {
                    continuation.resume(convertGroup(t[0].groupInfo))
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(null)
                }
            })
        }
    }

    override suspend fun getGroupMemberList(groupId: String): List<GroupMemberProfile> {
        var nextStep = 0L
        val memberList = mutableListOf<GroupMemberProfile>()
        while (true) {
            val pair = getGroupMemberList(groupId = groupId, nextStep = nextStep)
            memberList.addAll(pair.first)
            nextStep = pair.second
            if (nextStep <= 0) {
                break
            }
        }
        return memberList.sortedBy { it.joinTime }
    }

    private suspend fun getGroupMemberList(
        groupId: String,
        nextStep: Long
    ): Pair<List<GroupMemberProfile>, Long> {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getGroupManager().getGroupMemberList(groupId,
                V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL,
                nextStep,
                object : V2TIMValueCallback<V2TIMGroupMemberInfoResult> {
                    override fun onSuccess(t: V2TIMGroupMemberInfoResult) {
                        continuation.resume(
                            Pair(
                                convertGroupMember(t.memberInfoList),
                                t.nextSeq
                            )
                        )
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(Pair(emptyList(), -111))
                    }
                })
        }
    }

    private fun convertGroupMember(groupMemberList: List<V2TIMGroupMemberFullInfo>?): List<GroupMemberProfile> {
        return groupMemberList?.map { convertGroupMember(it) } ?: emptyList()
    }


    override suspend fun setAvatar(groupId: String, avatarUrl: String): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            val v2TIMGroupInfo = V2TIMGroupInfo()
            v2TIMGroupInfo.groupID = groupId
            v2TIMGroupInfo.faceUrl = avatarUrl
            V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, object : V2TIMCallback {
                override fun onSuccess() {
                    continuation.resume(ActionResult.Success)
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(ActionResult.Failed(code = code, reason = desc ?: ""))
                }
            })
        }
    }

}