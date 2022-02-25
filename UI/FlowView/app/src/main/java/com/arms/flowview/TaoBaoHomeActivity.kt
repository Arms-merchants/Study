package com.arms.flowview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.adapter.RvAdapter
import com.arms.flowview.configs.RouteUrl
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   : 仿淘宝首页的效果
 *    version: 1.0
 */
@Route(path = RouteUrl.TaoBaoEXUrl)
class TaoBaoHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_test)
        val rvHeader = findViewById<RecyclerView>(R.id.rv_header)
        val headerData = arrayListOf<String>()
        for (i in 0..3) {
            headerData.add("头部${i}")
        }
        rvHeader.adapter = RvAdapter(headerData)
        val vp2 = findViewById<ViewPager2>(R.id.vp2)
        val tab = findViewById<TabLayout>(R.id.tab_layout)
        val content = arrayListOf("test1", "test2", "test3")
        val fragments = arrayListOf<Fragment>(
            ListFragment.createInstance(0),
            ListFragment.createInstance(1),
            ListFragment.createInstance(2)
        )
        vp2.adapter = FAdapter(fragments, this)
        TabLayoutMediator(
            tab, vp2
        ) { tab, position ->
            tab.text = content[position]
        }.attach()

        val smartRefreshLayout = findViewById<SmartRefreshLayout>(R.id.refresh_layout)
        smartRefreshLayout.setOnMultiListener(object : SimpleMultiListener() {
            override fun onFooterMoving(
                footer: RefreshFooter?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                footerHeight: Int,
                maxDragHeight: Int
            ) {
                //offset底部出来的距离
                tab.y = (offset).toFloat()
            }
        })

        smartRefreshLayout.setOnRefreshListener {
            smartRefreshLayout.finishRefresh()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            smartRefreshLayout.postDelayed({
                smartRefreshLayout.finishLoadMore()
            }, 6000)
        }

    }

}