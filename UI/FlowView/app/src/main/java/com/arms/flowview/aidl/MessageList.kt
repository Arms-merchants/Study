package com.arms.flowview.aidl

import com.orhanobut.logger.Logger
import javax.inject.Inject

/**
 * author : heyueyang
 * time   : 2022/01/05
 * desc   :synchronized内置锁，如果是同一个对象的情况下，下面的例子中，当不同线程中有一个访问到了synchronized包裹的代码块时，
 * 在同一个对象下，其他的同样用这个对象的synchronized包裹的代码都会进入等待状态
 * 例如 线程1执行test1 线程2执行test2
 * 两个方法都会执行，但因为test1先执行获得到了锁，那么在test1执行完成之前（休眠了20000ms），test2中的logger是不会打印的
 * version: 1.0
 */
class MessageList @Inject constructor() {

    fun test1() {
        Logger.e("test1->${Thread.currentThread().name}")
        synchronized(this) {
            try {
                Logger.e("test1 is start")
                Thread.sleep(20000)
                Logger.e("test1 is end")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun test2() {
        Logger.e("test2->${Thread.currentThread().name}")
        synchronized(this) { Logger.e("test2 is run") }
    }
}