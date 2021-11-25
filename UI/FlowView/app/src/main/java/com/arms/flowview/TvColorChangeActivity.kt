package com.arms.flowview

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.view.ColorChangeTextView

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/10
 *    desc   :
 *    version: 1.0
 */
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
        val vpAdapter = FAdapter(arrayListOf(ListFragment(0), ListFragment(1)), this)
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
                    tab1.percent = 1-positionOffset
                    tab2.percent = positionOffset
                } else {
                    tab2.percent = 1-positionOffset
                    tab1.percent = positionOffset
                }
            }
        })
    }

}