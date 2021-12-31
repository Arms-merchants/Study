package com.arms.flowview.hilt

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   : 模拟仓库层
 *    version: 1.0
 */
@Singleton
class Repository @Inject constructor() {

    val data = MutableLiveData<Int>()

    fun getData():String{
        return "data"
    }
}