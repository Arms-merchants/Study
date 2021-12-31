package com.arms.flowview.aidl

import com.arms.flowview.IMyAidlInterface

/**
 *    author : heyueyang
 *    time   : 2021/12/29
 *    desc   :
 *    version: 1.0
 */
class MyAidlImp private constructor() : IMyAidlInterface.Stub() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MyAidlImp()
        }
    }

    override fun test(id: Int) {

    }

}