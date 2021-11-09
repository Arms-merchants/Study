package com.arms.flowview.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   :
 *    version: 1.0
 */
class FAdapter(private val fragments:ArrayList<Fragment>, fragmentActivity: FragmentActivity):FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
       return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}