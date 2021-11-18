package com.arms.flowview.ktx

import android.util.Log

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class KtxTest private constructor() {

    companion object {
        const val TAG = "========"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KtxTest()
        }
    }

    object CollectionKtx {

        fun test1() {
            //两个列表合并
            val combinedList = arrayListOf(1, 2, 3) + arrayListOf(4, 5, 6)
            // 这里的数据添加并不是在原集合上进行的，是一个新的集合
            var newList = arrayListOf(1, 2, 3) + 4 + 5
            newList = newList + 1
            newList.forEach {
                Log.e(TAG, "current${it}")
            }
        }

    }

}