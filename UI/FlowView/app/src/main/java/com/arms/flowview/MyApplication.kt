package com.arms.flowview

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.arms.flowview.utils.Utils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("TAG")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Utils.init(this)

        //Arouter初始化
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }

}