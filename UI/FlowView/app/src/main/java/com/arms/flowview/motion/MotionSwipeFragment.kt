package com.arms.flowview.motion

import android.annotation.SuppressLint
import android.view.Gravity
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentMotionSwipeBinding
import com.arms.flowview.rv.StarAdapter
import com.arms.flowview.rv.StarBean

/**
 *    author : heyueyang
 *    time   : 2021/12/15
 *    desc   :
 *    version: 1.0
 */
class MotionSwipeFragment : BaseBindingFragment<FragmentMotionSwipeBinding>() {
    @SuppressLint("WrongConstant")
    override fun initView() {
        val list = arrayListOf<StarBean>()
        for (i in 0..100) {
            list.add(StarBean(i.toString(), i.toString()))
        }
        binding.rv.adapter = StarAdapter(list)
    }
}