package com.arms.flowview

import com.alibaba.android.arouter.launcher.ARouter
import com.arms.flowview.utils.BlockCanary
import com.arms.flowview.utils.Utils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tojoy.app.kpi.application.DexLoadApplication
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */
@HiltAndroidApp
class MyApplication
    () : DexLoadApplication() {

    init {
        //Debug模式下进行方法追踪，不过华为手机有问题，小米手机可以获取到，不过需要注意需要文件写入权限，不然就只能写入到app的私有目录
        //Debug.startMethodTracing("enjoy")
    }

    override fun onCreate() {
        super.onCreate()
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        initLogger()
        initARouter()
        //BlockCanary.install()
    }

    private fun initARouter() {
        //Arouter初始化
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("TAG")
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        Utils.init(this)
    }

}