package com.arm.base.model

/**
 * @Author: leavesC
 * @Date: 2021/10/27 14:09
 * @Desc: 聊天，单聊或者是群组
 * @Github：https://github.com/leavesC
 */
sealed class Chat(val type: Int, val id: String) {

    companion object {

        fun find(type: Int, id: String): Chat {
            when (type) {
                1 -> {
                    return C2C(id)
                }
                2 -> {
                    return Group(id)
                }
            }
            throw IllegalArgumentException()
        }

    }

    class C2C(id: String) : Chat(1, id)

    class Group(id: String) : Chat(2, id)

}