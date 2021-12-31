package com.arms.flowview.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.arms.flowview.IMyAidlInterface

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   :
 *    version: 1.0
 */
class TestServiceCommection : ServiceConnection {

    /**
     * 通过bind启动一个service，并通过ServiceConnection获取其链接状态
     */
    fun initAidlConnection(context: Context) {
        val intent = Intent(context, TestService::class.java)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        //获得对应的Binder
        val binder = IMyAidlInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

    override fun onBindingDied(name: ComponentName?) {
        super.onBindingDied(name)
    }

}