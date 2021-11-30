package com.arm.base.model

import com.arm.base.utils.TimeUtil


/**
 * @Author: leavesC
 * @Date: 2021/6/20 23:40
 * @Desc:
 * @Github：https://github.com/leavesC
 */
/**
 * 消息状态
 */
sealed class MessageState {

    /**
     * 发送中
     */
    object Sending : MessageState()

    /**
     * 发送失败
     */
    object SendFailed : MessageState()

    /**
     * 发送完成
     */
    object Completed : MessageState()

}

/**
 * 消息
 * 消息id[timestamp],时间戳[timestamp],消息状态[state]，发送方[sender]
 */
sealed class Message(
    val msgId: String,
    val timestamp: Long,
    val state: MessageState,
    val sender: BaseProfile,
) {

    val conversationTime by lazy {
        TimeUtil.toConversationTime(timestamp)
    }

    val time by lazy {
        TimeUtil.toChatTime(timestamp)
    }

    var tag: Any? = null

    override fun toString(): String {
        return "Message(msgId='$msgId', timestamp=$timestamp, state=$state, sender=$sender, conversationTime='$conversationTime', chatTime='$time', tag=$tag)"
    }

}

/**
 * 事件消息
 */
class TimeMessage(
    targetMessage: Message
) : Message(
    msgId = (targetMessage.timestamp + targetMessage.msgId.hashCode()).toString(),
    timestamp = targetMessage.timestamp,
    state = MessageState.Completed,
    sender = PersonProfile.Empty,
)

/**
 * 文本消息
 */
sealed class TextMessage(
    msgId: String,
    timestamp: Long,
    state: MessageState,
    sender: BaseProfile,
    val msg: String,
) : Message(
    msgId = msgId,
    timestamp = timestamp,
    state = state,
    sender = sender
) {
    /**
     * 自己发出的消息
     */
    class SelfTextMessage(
        msgId: String,
        state: MessageState,
        timestamp: Long,
        sender: BaseProfile,
        msg: String,
    ) : TextMessage(
        msgId = msgId,
        timestamp = timestamp,
        state = state,
        sender = sender,
        msg = msg
    )

    /**
     * 好友的消息
     */
    class FriendTextMessage(
        msgId: String,
        timestamp: Long,
        sender: BaseProfile,
        msg: String,
    ) : TextMessage(
        msgId = msgId,
        timestamp = timestamp,
        state = MessageState.Completed,
        sender = sender,
        msg = msg
    )

    fun copy(
        msgId: String = this.msgId,
        msg: String = this.msg,
        timestamp: Long = this.timestamp,
        state: MessageState = this.state,
        sender: BaseProfile = this.sender
    ): TextMessage {
        return when (this) {
            is FriendTextMessage -> {
                FriendTextMessage(
                    msgId = msgId,
                    msg = msg,
                    timestamp = timestamp,
                    sender = sender,
                )
            }
            is SelfTextMessage -> {
                SelfTextMessage(
                    msgId = msgId,
                    msg = msg,
                    state = state,
                    timestamp = timestamp,
                    sender = sender,
                )
            }
        }
    }

}