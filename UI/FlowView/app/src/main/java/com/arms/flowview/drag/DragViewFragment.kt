package com.arms.flowview.drag

import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentDragViewBinding
import com.arms.flowview.rv.StarAdapter
import com.arms.flowview.rv.StarBean

/**
 *    author : heyueyang
 *    time   : 2021/12/14
 *    desc   :
 *    version: 1.0
 */
class DragViewFragment : BaseBindingFragment<FragmentDragViewBinding>() {
    override fun initView() {
        val list = arrayListOf<StarBean>()
        for (i in 0..100) {
            list.add(StarBean(i.toString(), i.toString()))
        }
        val adapter = StarAdapter(null)
        binding.rv.adapter = adapter
        adapter.setNewInstance(list)
    }
}