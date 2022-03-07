package com.arms.flowview.utils

import android.app.AlarmManager
import android.content.Context
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
        //在handler执行的前后打印日志，这里替换为自己实现的
        Looper.getMainLooper().setMessageLogging(logMonitor)
/*        val context:Context
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact()
        alarmManager.setAndAllowWhileIdle()*/

    }
}