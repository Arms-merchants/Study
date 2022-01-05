package com.arms.flowview.aidl

import android.app.Application
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.FragmentListBinding
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ProcessUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2022/01/05
 *    desc   : 测试使用hilt来注入Aidl的service进行链接的类,当前类单独定义一个进程，启动service都是在单独定义的进程
 *    version: 1.0
 */
@AndroidEntryPoint
@Route(path = RouteUrl.AIDLTESTURL)
class AidleTestActivity : BaseBindingActivity<FragmentListBinding>() {

    @Inject
    lateinit var serviceCommection: TestServiceCommection

    override fun initView() {
        "AidleTestActivity current process :${ProcessUtils.getCurrentProcessName(this)}".logE()
        serviceCommection.initAidlConnection(this)
    }
}