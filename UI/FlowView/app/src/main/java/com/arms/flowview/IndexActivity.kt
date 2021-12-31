package com.arms.flowview

import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.launcher.ARouter
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityIndexBinding
import com.arms.flowview.databinding.ItemIndexBinding
import com.arms.flowview.ktx.KtxTestActivity
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder
import com.arms.flowview.rv.RvListActivity
import com.arms.flowview.singleac.SingleActivity
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
        val list = arrayListOf<Pair<String, String>>(
            "FlowView" to RouteUrl.FlowViewUrl,
            "FishView" to RouteUrl.FishViewUrl,
            "ViewPager" to RouteUrl.ViewPageUrl,
            "ViewPage2" to RouteUrl.ViewPager2Url,
            "RecyclerView" to RouteUrl.RecyclerViewUrl,
            "TextColorChange" to RouteUrl.TextColorChangeUrl,
            "仿淘宝" to RouteUrl.TaoBaoEXUrl,
            "KTX相关的测试" to RouteUrl.KtxTestUrl,
            "单Activity" to RouteUrl.SingleActivityUrl,
            "BehaviorTest" to RouteUrl.BehaviorTestUrl,
            "HiltTest" to RouteUrl.HILTESTURL
        )
        binding.rv.adapter = object :
            BaseRecyclerViewAdapter<Pair<String, String>, ItemIndexBinding, BaseViewBindingHolder<ItemIndexBinding>>(
                list
            ) {
            override fun convert(
                holder: BaseViewBindingHolder<ItemIndexBinding>,
                item: Pair<String, String>?
            ) {
                holder.vb.tv.text = item?.first
                holder.vb.tv.setOnClickListener {
                    //Arouter通过路径跳转

                    ARouter.getInstance().build(item?.second).navigation()
                 /*   ARouter.getInstance().build(item?.second)
                        .withBoolean("isTest",false)
                        .withObject()*/

                }
            }
        }
    }
}