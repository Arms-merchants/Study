package com.arm.proxy.coroutineScope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   :
 *    version: 1.0
 */
/**
 * internal 限定在当前model中使用
 * 定义一个协程的作用域
 */
internal class ChatCoroutineScope :CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob()+Dispatchers.Main.immediate

    fun cancel(){
        coroutineContext.cancel()
    }

}