package com.arm.proxy.utils

import kotlin.random.Random

/**
 * @Author: leavesC
 * @Date: 2021/7/7 17:17
 * @Desc: 一个随机数的工具类
 * @Github：https://github.com/leavesC
 */
object RandomUtils {

    private val randomInt: Int
        get() = Random.nextInt()

    private val randomLong: Long
        get() = Random.nextLong()

    /**
     * 生产一个消息id
     */
    fun generateMessageId(): String {
        return (System.currentTimeMillis() + randomLong + randomLong).toString()
    }

}