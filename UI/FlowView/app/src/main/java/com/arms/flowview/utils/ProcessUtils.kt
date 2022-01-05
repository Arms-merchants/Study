package com.arms.flowview.utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import java.lang.reflect.Method


/**
 *    author : heyueyang
 *    time   : 2022/01/05
 *    desc   :
 *    version: 1.0
 */
object ProcessUtils {

    fun getCurrentProcessName(context: Context): String? {
        var currentProcessName: String? = null
        currentProcessName = getCurrentProcessNameByApplication()
        if (!currentProcessName.isNullOrEmpty()) {
            return currentProcessName
        }
        currentProcessName = getCurrentProcessNameByActivityThread()
        if (!currentProcessName.isNullOrEmpty()) {
            return currentProcessName
        }
        currentProcessName = getCurrentProcessNameByActivityManager(context)
        if (!currentProcessName.isNullOrEmpty()) {
            return currentProcessName
        }
        return null
    }

    private fun getCurrentProcessNameByApplication(): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        return null
    }

    private fun getCurrentProcessNameByActivityThread(): String? {
        var processName: String? = null
        try {
            val declaredMethod: Method = Class.forName(
                "android.app.ActivityThread", false,
                Application::class.java.classLoader
            )
                .getDeclaredMethod("currentProcessName", *arrayOfNulls<Class<*>?>(0))
            declaredMethod.isAccessible = true
            val invoke: Any = declaredMethod.invoke(null, arrayOfNulls<Any>(0))
            if (invoke is String) {
                processName = invoke
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return processName
    }

    private fun getCurrentProcessNameByActivityManager(context: Context): String? {
        val pid: Int = Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppList = am.runningAppProcesses
        if (runningAppList != null) {
            for (processInfo in runningAppList) {
                if (processInfo.pid == pid) {
                    return processInfo.processName
                }
            }
        }
        return null
    }


}