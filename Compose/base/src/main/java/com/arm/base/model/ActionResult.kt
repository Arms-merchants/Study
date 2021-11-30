package com.arm.base.model

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   :
 *    version: 1.0
 */

/**
 * 事件的结果类
 * sealed 密封类，相当于约束类，只能通过它的子类去访问
 */
sealed class ActionResult {

    object Success:ActionResult()

    data class Failed(val code:Int,val reason:String):ActionResult(){
        constructor(reason: String):this(code = -1,reason = reason)
    }
}

/**
 * 加载更多的状态
 */
sealed class LoadMessageResult{
    data class Success(val messageList:List<Message>,val loadFinish:Boolean):LoadMessageResult()
    data class Failed(val reason: String):LoadMessageResult()
}

/**
 * 服务器的状态
 */
enum class ServerState {
    Nothing,
    Logout,
    Connecting,
    ConnectSuccess,
    ConnectFailed,
    UserSigExpired,
    KickedOffline
}