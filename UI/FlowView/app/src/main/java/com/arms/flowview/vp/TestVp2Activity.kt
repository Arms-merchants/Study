package com.arms.flowview.vp

import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.databinding.ActivityVp2TestBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 *    author : heyueyang
 *    time   : 2021/11/25
 *    desc   :
 *    version: 1.0
 */
class TestVp2Activity : BaseBindingActivity<ActivityVp2TestBinding>() {

    override fun initView() {
        val content = arrayListOf<String>("tab1", "tab2", "tab3")
        val title = arrayOf("tab1", "tab2", "tab3")
        val list = arrayListOf<Fragment>(
            FragmentVp2.newInstance(0),
            FragmentVp2.newInstance(1),
            FragmentVp2.newInstance(2)
        )
        binding.vp2.adapter = FAdapter(list, this)

 /*       TabLayoutMediator(
            binding.tabLayout, binding.vp2
        ) { tab, position ->
            tab.text = content[position]
        }.attach()*/

        binding.tabLayout.setViewPage2(binding.vp2,title)
    }

}