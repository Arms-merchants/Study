package com.arm.composechat.model

/**
 * @Author: leavesC
 * @Date: 2021/7/27 10:39
 * @Desc:
 * @Github：https://github.com/leavesC
 */
enum class AppTheme(val type: Int) {

    Blue(0), Dark(1), Pink(2), Light(3);

    fun nextTheme(): AppTheme {
        val values = values()
        //getOrElse如果下标越界了就返回给定的元素，这里是默认返回第0个
        return values.getOrElse(type + 1) {
            values[0]
        }
    }

}