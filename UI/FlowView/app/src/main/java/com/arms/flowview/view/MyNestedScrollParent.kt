package com.arms.flowview.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper

/**
 *    author : heyueyang
 *    time   : 2021/12/08
 *    desc   :
 *    version: 1.0
 */
class MyNestedScrollParent : LinearLayout, NestedScrollingParent {

    private val parentHelper: NestedScrollingParentHelper

    init {
        parentHelper = NestedScrollingParentHelper(this)
    }

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        parentHelper.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStopNestedScroll(target: View) {
        parentHelper.onStopNestedScroll(target)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val hideTop = hideTop(dy)
        val showTop = showTop(dy)
        "hideTop:${hideTop} --- showTop:{$showTop} --- dy:{$dy}"
        if (hideTop(dy) || showTop(dy)) {
            consumed[1] = dy
            scrollBy(0, dy)
        }
    }

    private fun hideTop(dy: Int): Boolean {
        if (dy > 0 && scrollY < getChildAt(0).measuredHeight) {
            return true
        }
        return false
    }

    private fun showTop(dy: Int): Boolean {
        val view = getChildAt(0)
        if (dy < 0 && scrollY > 0 && !view.canScrollVertically(-1)) {
            return true
        }
        return false
    }


}