package com.arms.flowview.vp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 *    author : heyueyang
 *    time   : 2021/11/24
 *    desc   :
 *    version: 1.0
 */
class MyAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val list = arrayListOf<Fragment>(
        TestFragment(0),
        TestFragment(1),
        TestFragment(2),
        TestFragment(3),
        TestFragment(4)
    )

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}