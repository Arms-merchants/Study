package com.arm.base.provider

import com.arm.base.model.ActionResult
import com.arm.base.model.GroupMemberProfile
import com.arm.base.model.GroupProfile
import kotlinx.coroutines.flow.StateFlow

/**
 * @Author: leavesC
 * @Date: 2021/7/12 0:04
 * @Desc:
 * @Github：https://github.com/leavesC
 */
interface IGroupProvider {

    val joinedGroupList: StateFlow<List<GroupProfile>>

    fun getJoinedGroupList()

    suspend fun joinGroup(groupId: String): ActionResult

    suspend fun quitGroup(groupId: String): ActionResult

    suspend fun getGroupInfo(groupId: String): GroupProfile?

    suspend fun getGroupMemberList(groupId: String): List<GroupMemberProfile>

    suspend fun setAvatar(groupId: String, avatarUrl: String): ActionResult

}