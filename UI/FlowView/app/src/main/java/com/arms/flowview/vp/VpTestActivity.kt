package com.arms.flowview.vp

import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityVpTestBinding
import com.google.android.material.tabs.TabLayout

/**
 *    author : heyueyang
 *    time   : 2021/11/24
 *    desc   :
 *    version: 1.0
 */
@Route(path = RouteUrl.ViewPageUrl)
class VpTestActivity : BaseBindingActivity<ActivityVpTestBinding>() {
    override fun initView() {
        binding.apply {
            tabLayout.addTab(tabLayout.newTab().setText("tab0"))
            tabLayout.addTab(tabLayout.newTab().setText("tab1"))
            tabLayout.addTab(tabLayout.newTab().setText("tab2"))
            tabLayout.addTab(tabLayout.newTab().setText("tab3"))
            tabLayout.addTab(tabLayout.newTab().setText("tab4"))
            vp.adapter = MyAdapter(supportFragmentManager)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { vp.currentItem = it }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        }
    }
}


