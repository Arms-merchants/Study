package com.arm.base.provider

import com.arm.base.model.Chat
import com.arm.base.model.LoadMessageResult
import com.arm.base.model.Message
import kotlinx.coroutines.channels.Channel

/**
 * @Author: leavesC
 * @Date: 2021/6/22 11:02
 * @Desc:
 * @Github：https://github.com/leavesC
 */
interface IMessageProvider {

    interface MessageListener {

        fun onReceiveMessage(message: Message)

    }

    fun startReceive(chat: Chat, messageListener: MessageListener)

    fun stopReceive(messageListener: MessageListener)

    suspend fun send(
        chat: Chat,
        text: String,
        channel: Channel<Message>
    )

    suspend fun getHistoryMessage(chat: Chat, lastMessage: Message?): LoadMessageResult

    fun markMessageAsRead(chat: Chat)

}