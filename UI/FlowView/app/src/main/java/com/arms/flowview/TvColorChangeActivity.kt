package com.arms.flowview

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.view.ColorChangeTextView

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/10
 *    desc   : 文本变色的绘制，就是通过Canvas.clipRect来进行裁剪矩形状来实现，绘制两个两个文本，通过进度控制裁剪区域从而实现文本的变色效果
 *    下面的实现效果还有问题，需要而且文本的绘制方向也没有实现。
 *    version: 1.0
 */
@Route(path = RouteUrl.TextColorChangeUrl)
class TvColorChangeActivity : AppCompatActivity() {

    private lateinit var tab1: ColorChangeTextView
    private lateinit var tab2: ColorChangeTextView
    private lateinit var vp2: ViewPager2

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_tv)
        vp2 = findViewById(R.id.vp2)
        tab1 = findViewById(R.id.tv_1)
        tab2 = findViewById(R.id.tv_2)
        val vpAdapter = FAdapter(
            arrayListOf(ListFragment.createInstance(0), ListFragment.createInstance(1)),
            this
        )
        vp2.adapter = vpAdapter
        vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            /**
             *
             *
             * @param position       当前显示的index
             * @param positionOffset       移动的偏移量比例[0, 1)
             * @param positionOffsetPixels 具体的偏移位置量，像素
             */
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 0) {
                    tab1.percent = 1 - positionOffset
                    tab2.percent = positionOffset
                } else {
                    tab2.percent = 1 - positionOffset
                    tab1.percent = positionOffset
                }
            }
        })
    }

}