package com.arms.flowview.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper

class MyNestedScrollParentL : LinearLayout, NestedScrollingParent {
    private var helper: NestedScrollingParentHelper? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val view = getChildAt(1)
        val layoutParams = view.layoutParams
        layoutParams.height = measuredHeight
        view.layoutParams = layoutParams
    }


    private fun init(context: Context) {
        helper = NestedScrollingParentHelper(this)
    }

    /**
     * @param child
     * @param target
     * @param nestedScrollAxes 嵌套滑动的坐标系，也就是用来判断X轴滑动还是Y轴滑动，这里可以根据需要返回true和false
     * @return 返回false就没法滑动了
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        Log.i(
            Tag,
            "onStartNestedScroll--child:$child,target:$target,nestedScrollAxes:$nestedScrollAxes"
        )
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        Log.i(
            Tag,
            "onNestedScrollAcceptedchild:$child,target:$target,nestedScrollAxes:$nestedScrollAxes"
        )
        helper!!.onNestedScrollAccepted(child, target, nestedScrollAxes)
    }

    override fun onStopNestedScroll(target: View) {
        Log.i(Tag, "onStopNestedScroll--target:$target")
        helper!!.onStopNestedScroll(target)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        Log.i(
            Tag,
            "onNestedScroll--" + "target:" + target + ",dxConsumed" + dxConsumed + ",dyConsumed:" + dyConsumed
                    + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" + dyUnconsumed
        )
    }

    /**
     * 在滑动之前会被调用，他的作用就是子类在滑动的时候，分发一下，是否有父类需要消费滑动，这个时候，父类就可以根据自己的业务逻辑进行消费部分和全部消费
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        val show = showImg(dy)
        val hide = hideImg(dy)
        //        Log.i("onMeasure","show: " + show + ", hide: " + hide);
        if (show || hide) { //1.作业 消费过头
            consumed[1] = dy //全部消费
            scrollBy(0, dy)
            Log.i(Tag, "Parent滑动：$dy")
        }
        Log.i(
            Tag,
            "onNestedPreScroll--getScrollY():" + scrollY + ",dx:" + dx + ",dy:" + dy + ",consumed:" + consumed[1]
        )
    }

    private fun showImg(dy: Int): Boolean {
        val view = getChildAt(0)
        //        Log.i("onMeasure","showImg: " + dy + " height: " + view.getHeight() + " getScrollY: " + getScrollY() + " can: " + view.canScrollVertically(-1));
        return dy < 0 && scrollY > 0 && !view.canScrollVertically(-1)
    }

    private fun hideImg(dy: Int): Boolean {
        val view = getChildAt(0)
        //        Log.i("onMeasure","showImg: " + dy + " height: " + view.getHeight() + " getScrollY: " + getScrollY() + " can: " + view.canScrollVertically(-1));
        return dy > 0 && scrollY < view.height
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.i(Tag, "onNestedFling--target:$target")
        return true
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        Log.i(Tag, "onNestedPreFling--target:$target")
        return true
    }

    companion object {
        private const val Tag = "Zero"
    }
}