package com.arms.flowview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/10
 *    desc   : 自定义View 用一个布局来承接FishView，达到鱼游动的效果和水波纹的绘制实现
 *    version: 1.0
 */
class TestFishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fish)
    }

}