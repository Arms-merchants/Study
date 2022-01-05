package com.arms.flowview.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.arms.flowview.IMyAidlInterface
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ProcessUtils
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   : 用于监听服务状态的类
 *    version: 1.0
 */
@ActivityScoped
class TestServiceCommection @Inject constructor() : ServiceConnection {

    lateinit var mContext: Context

    /**
     * 通过bind启动一个service，并通过ServiceConnection获取其链接状态
     */
    fun initAidlConnection(context: Context) {
        mContext = context
        "TestServiceCommection initAidlConnection ${ProcessUtils.getCurrentProcessName(context)}".logE()
        val intent = Intent(context, TestService::class.java)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    /**
     * 在与 Service 的连接建立时调用，使用与IBinder的通信通道的IBinder 。
    注意：如果系统已开始将您的客户端应用程序绑定到服务，则您的应用程序可能永远不会收到此回调。
    如果服务出现问题，例如服务在创建时崩溃，您的应用将不会收到回调。
     */
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        "TestServiceCommection onServiceConnected ${ProcessUtils.getCurrentProcessName(mContext)}".logE()
        //获得对应的Binder
        val binder = IMyAidlInterface.Stub.asInterface(service)
        binder.test(7321)
    }

    /**
     * 当与服务的连接丢失时调用。 这通常发生在托管服务的进程崩溃或被终止时。
     * 这不会删除ServiceConnection本身-这种结合的服务将保持活动状态，
     * 您将收到一个来电onServiceConnected当服务是未来的运行。
     */
    override fun onServiceDisconnected(name: ComponentName?) {
        "TestServiceCommection onServiceDisconnected ${ProcessUtils.getCurrentProcessName(mContext)}".logE()
    }

    /**
     * 当与此连接的绑定失效时调用。 这意味着该接口将永远不会收到另一个连接。
     * 应用程序将需要解除绑定并重新绑定连接以再次激活它。
     * 例如，如果托管它所绑定的服务的应用程序已更新，则可能会发生这种情况。
     */
    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
        "TestServiceCommection onBindingDied ${ProcessUtils.getCurrentProcessName(mContext)}".logE()
    }

}