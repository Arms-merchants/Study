package com.arm.composechat.logic

import com.arm.base.provider.IAccountProvider
import com.arm.base.provider.IConversationProvider
import com.arm.base.provider.IGroupProvider
import com.arm.base.provider.IMessageProvider
import com.arm.proxy.logic.*
import github.leavesc.compose_chat.base.provider.IFriendshipProvider

/**
 * @Author: leavesC
 * @Date: 2021/6/22 11:35
 * @Desc:
 * @Github：https://github.com/leavesC
 */
object ComposeChat {

    const val groupIdA = "@TGS#3KGRTDSHY"

    const val groupIdB = "@TGS#3S4RTDSHN"

    const val groupIdC = "@TGS#3QXSTDSHR"

    const val groupIdD = "@TGS#3GR2HLTH3"

    const val groupIdE = "@TGS#3Q65MLTHR"

    const val groupIdF = "@TGS#3XFHNLTH5"

    val accountProvider: IAccountProvider = AccountProvider()

    val conversationProvider: IConversationProvider = ConversationProvider()

    val messageProvider: IMessageProvider = MessageProvider()

    val friendshipProvider: IFriendshipProvider = FriendshipProvider()

    val groupProvider: IGroupProvider = GroupProvider()

}