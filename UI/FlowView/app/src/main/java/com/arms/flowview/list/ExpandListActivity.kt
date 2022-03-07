package com.arms.flowview.list

import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityExpandListBinding
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
@Route(path = RouteUrl.EXPAND_LIST)
class ExpandListActivity : BaseBindingActivity<ActivityExpandListBinding>() {
    override fun initView() {
        val heads = mutableListOf<BaseNode>()
        for (i in 0..10) {
            val childs = mutableListOf<BaseNode>()
            for (y in 0..3) {
                val body = Body("body${y}")
                childs.add(body)
            }
            val head = Head(childs, "Head${i}")
            heads.add(head)
        }
        val adapter = ExpandAdapter()
        binding.rv.adapter = adapter
        adapter.setNewInstance(heads)
    }
}