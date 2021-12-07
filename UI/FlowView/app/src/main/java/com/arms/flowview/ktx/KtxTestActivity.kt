package com.arms.flowview.ktx

import android.content.Intent
import android.os.Bundle
import com.arms.flowview.*
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.databinding.ActivityIndexBinding
import com.arms.flowview.databinding.ItemIndexBinding
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */
class KtxTestActivity : BaseBindingActivity<ActivityIndexBinding>() {

    override fun initView() {

        val list = arrayListOf<Pair<String, Class<*>>>(
            "LiveData" to LiveDataTestActivity::class.java
        )
        binding.rv.adapter = object :
            BaseRecyclerViewAdapter<Pair<String, Class<*>>, ItemIndexBinding, BaseViewBindingHolder<ItemIndexBinding>>(
                list
            ) {
            override fun convert(
                holder: BaseViewBindingHolder<ItemIndexBinding>,
                item: Pair<String, Class<*>>?
            ) {
                holder.vb.tv.text = item?.first
                holder.vb.tv.setOnClickListener {
                    item?.second?.let { clazz ->
                        startActivity(Intent(this@KtxTestActivity, clazz))
                    }
                }

            }
        }
    }
}