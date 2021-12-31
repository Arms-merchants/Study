package com.arms.flowview.autoservice

import com.arms.flowview.ext.logE
import com.google.auto.service.AutoService

/**
 *    author : heyueyang
 *    time   : 2021/12/28
 *    desc   :
 *    version: 1.0
 */
@AutoService(ITest::class)
class TestImp : ITest {
    override fun setId(id: Int) {
        "Id${id}".logE()
    }

    override fun getIdValue(): Int {
        return 0
    }
}