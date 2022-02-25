package com.arms.flowview.utils

import android.view.Choreographer

/**
 *    author : heyueyang
 *    time   : 2022/02/25
 *    desc   : 一个进行桢率检测的工具，就是检测上一桢和下一桢之间的间隔时间是否大于16.6
 *    version: 1.0
 */
class ChoreographerHelper {

    var lastFrameTime = 0L

    fun start() {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (lastFrameTime == 0L) {
                    lastFrameTime = frameTimeNanos
                    Choreographer.getInstance().postFrameCallback(this)
                    return
                }
                val diff = (frameTimeNanos - lastFrameTime) / 1_000_000
                if (diff > 16.6f) {
                    val droppedCount = (diff / 16.6).toInt()
                }
                lastFrameTime = frameTimeNanos
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }
}