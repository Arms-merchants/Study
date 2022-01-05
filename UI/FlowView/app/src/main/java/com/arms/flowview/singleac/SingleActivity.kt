package com.arms.flowview.singleac

import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivitySingleBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *    author : heyueyang
 *    time   : 2021/12/07
 *    desc   :
 *    version: 1.0
 */
@AndroidEntryPoint
@Route(path = RouteUrl.SingleActivityUrl)
class SingleActivity : BaseBindingActivity<ActivitySingleBinding>() {

    override fun initView() {

    }
}