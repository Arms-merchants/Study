package com.arms.flowview.utils

import android.os.Looper

/**
 *    author : heyueyang
 *    time   : 2022/02/25
 *    desc   :
 *    version: 1.0
 */
object BlockCanary {

    fun install() {
        val logMonitor = LogMonitor()
        Looper.getMainLooper().setMessageLogging(logMonitor)
    }
}