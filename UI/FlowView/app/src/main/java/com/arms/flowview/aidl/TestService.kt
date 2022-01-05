package com.arms.flowview.aidl

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ProcessUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   :
 *    version: 1.0
 */
@AndroidEntryPoint
class TestService() : Service() {
    @Inject
    lateinit var myAidlImp: MyAidlImp

    @ApplicationContext
    @Inject
    lateinit var context: Context

    /**
     * 将通信通道返回给服务。 如果客户端无法绑定到服务，则可能返回 null。 返回的IBinder通常用于使用 aidl 描述的复杂接口。
    请注意，与其他应用程序组件不同，对此处返回的 IBinder 接口的调用可能不会发生在进程的主线程上。 有关主线程的更多信息可以在进程和线程中找到
     */
    override fun onBind(intent: Intent?): IBinder? {
        "TestService onBind ${ProcessUtils.getCurrentProcessName(context)}".logE()
        return myAidlImp.asBinder()
    }

    /**
     * 断开与应用程序服务的连接。 服务重新启动后，您将不再接听电话，现在可以随时停止服务。
     */
    override fun unbindService(conn: ServiceConnection) {
        "TestService unbindService ${ProcessUtils.getCurrentProcessName(context)}".logE()
        super.unbindService(conn)
    }

}