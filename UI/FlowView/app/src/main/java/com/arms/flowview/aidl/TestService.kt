package com.arms.flowview.aidl

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   :
 *    version: 1.0
 */
class TestService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return MyAidlImp.instance.asBinder()
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

}