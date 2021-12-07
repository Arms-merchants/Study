package com.arms.flowview

import android.content.Intent
import android.os.Bundle
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.databinding.ActivityIndexBinding
import com.arms.flowview.databinding.ItemIndexBinding
import com.arms.flowview.ktx.KtxTestActivity
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder
import com.arms.flowview.vp.TestVp2Activity
import com.arms.flowview.vp.VpTestActivity

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :索引列表页
 *    version: 1.0
 */
class IndexActivity : BaseBindingActivity<ActivityIndexBinding>() {

    override fun initView() {
        val list = arrayListOf<Pair<String, Class<*>>>(
            "FlowView" to MainActivity::class.java,
            "FishView" to TestFishActivity::class.java,
            "ViewPager" to VpTestActivity::class.java,
            "ViewPage2" to TestVp2Activity::class.java,
            "RecyclerView" to RvListActivity::class.java,
            "TextColorChange" to TvColorChangeActivity::class.java,
            "仿淘宝" to TestActivity::class.java,
            "KTX相关的测试" to KtxTestActivity::class.java
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
                        startActivity(Intent(this@IndexActivity, clazz))
                    }
                }

            }
        }
    }
}