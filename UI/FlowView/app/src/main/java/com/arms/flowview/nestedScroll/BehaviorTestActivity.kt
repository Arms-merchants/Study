package com.arms.flowview.nestedScroll

import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityCoordinatorlayoutBinding
import com.arms.flowview.databinding.ItemIndexBinding
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder

/**
 *    author : heyueyang
 *    time   : 2021/12/20
 *    desc   :
 *    version: 1.0
 */
@Route(path = RouteUrl.BehaviorTestUrl)
class BehaviorTestActivity:BaseBindingActivity<ActivityCoordinatorlayoutBinding>() {
    override fun initView() {
        val list = arrayListOf<String>()
        for(i in 0..100){
            list.add("current:${i}")
        }
        binding.rv.adapter = object :BaseRecyclerViewAdapter<String, ItemIndexBinding,BaseViewBindingHolder<ItemIndexBinding>>(list){
            override fun convert(holder: BaseViewBindingHolder<ItemIndexBinding>, item: String?) {
                    holder.vb.tv.text = item
            }
        }
    }
}