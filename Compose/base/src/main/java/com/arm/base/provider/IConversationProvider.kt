package com.arm.base.provider


import com.arm.base.model.ActionResult
import com.arm.base.model.Conversation
import kotlinx.coroutines.flow.StateFlow


/**
 * @Author: leavesC
 * @Date: 2021/6/22 12:01
 * @Desc:
 * @Github：https://github.com/leavesC
 */
interface IConversationProvider {

    val conversationList: StateFlow<List<Conversation>>

    val totalUnreadCount: StateFlow<Long>

    fun getConversationList()

    suspend fun pinConversation(conversation: Conversation, pin: Boolean): ActionResult

    suspend fun deleteConversation(conversation: Conversation): ActionResult

}