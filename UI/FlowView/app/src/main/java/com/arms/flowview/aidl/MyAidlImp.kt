package com.arms.flowview.aidl

import android.content.Context
import com.arms.flowview.IMyAidlInterface
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ProcessUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   : 实现Aidl接口的的Stub类型，实现
 *
 *
 *    version: 1.0
 */
class MyAidlImp @Inject constructor(@ApplicationContext val context: Context) :
    IMyAidlInterface.Stub() {

    override fun test(id: Int) {
        //主进程，也即是通过Aidl实现了从.aidl_test进程发送数据到了主进程中
        "MyAidlImp test ${ProcessUtils.getCurrentProcessName(context)} ---${id}".logE()
    }

}