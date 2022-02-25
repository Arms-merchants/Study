package com.arms.flowview.utils

import android.content.Context

/**
 *    author : heyueyang
 *    time   : 2022/02/16
 *    desc   :
 *    version: 1.0
 */
class FileUtils {

    fun path(context:Context){
        //app私有的目录,不需要权限
        val dir = context.getExternalFilesDir("apk")
        //此应用程序的基本 APK 的完整路径。
        context.applicationInfo.sourceDir
    }

}