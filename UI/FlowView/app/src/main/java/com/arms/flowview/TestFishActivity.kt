package com.arms.flowview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.configs.RouteUrl
import com.orhanobut.logger.Logger

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/10
 *    desc   : 自定义View 用一个布局来承接FishView，达到鱼游动的效果和水波纹的绘制实现
 *    version: 1.0
 */
@Route(path = RouteUrl.FishViewUrl)
class TestFishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fish)

        Logger.e("123123123")
    }

}