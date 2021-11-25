package com.arms.flowview.vp

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.arms.flowview.ext.logE

/**
 *    author : heyueyang
 *    time   : 2021/11/24
 *    desc   :
 *    version: 1.0
 */
class TestViewPager : ViewPager {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, att: AttributeSet) : super(context, att) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        "onMeasure".logE()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        "onLayout".logE()
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        "setAdapter".logE()
    }

}