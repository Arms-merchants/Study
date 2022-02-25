package com.arms.flowview.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import android.view.ViewConfiguration
import android.view.VelocityTracker
import android.view.MotionEvent
import android.widget.ScrollView
import androidx.core.view.ViewCompat
import com.arms.flowview.ext.logE
import com.orhanobut.logger.Logger

/**
 * author : heyueyang
 * time   : 2021/12/09
 * desc   :
 * version: 1.0
 */
internal class MyNestedScrollChild : ScrollView, NestedScrollingChild {
    private val mScrollingChildHelper = NestedScrollingChildHelper(this)
    private var mMinFlingVelocity = 0
    private var mMaxFlingVelocity = 0

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

    private fun init(context: Context) {
        val vc = ViewConfiguration.get(context)
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity
        mMaxFlingVelocity = vc.scaledMaximumFlingVelocity
        mScrollingChildHelper.isNestedScrollingEnabled = true
    }

    /**
     * 开启一个嵌套滑动
     *
     * @param axes 支持的嵌套滑动方法，分为水平滑动，竖向滑动，或不确定
     * @return 如果返回true，表示当前子view已经知道了一起嵌套滑动的view
     */
    override fun startNestedScroll(axes: Int): Boolean {
        return mScrollingChildHelper.startNestedScroll(axes)
    }

    /**
     * 在子view滑动前，将事件分发给父view，由父view判断消耗多少
     *
     * @param dx             水平方向嵌套滑动的子View想要变化的距离 dx<0 向右滑动 dx>0 向左滑动
     * @param dy             垂直方向嵌套滑动的子View想要变化的距离 dy<0 向下滑动 dy>0 向上滑动
     * @param consumed       子view传给父view数组，用于存储父view水平与竖直方向上消耗的距离，consumed[0]
     * 水平消耗的距离，consumed[1] 垂直消耗的距离
     * @param offsetInWindow 子view在当前window的偏移量
     * @return 如果返回true, 表示父view已经消耗了
     */
    override fun dispatchNestedPreScroll(
        dx: Int, dy: Int, consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    /**
     * 当父view消耗事件后，子view处理后，又继续将事件分发给父view,由父view判断是否消耗剩下的距离。
     *
     * @param dxConsumed     水平方向嵌套滑动的子View滑动的距离(消耗的距离)
     * @param dyConsumed     垂直方向嵌套滑动的子View滑动的距离(消耗的距离)
     * @param dxUnconsumed   水平方向嵌套滑动的子View未滑动的距离(未消耗的距离)
     * @param dyUnconsumed   垂直方向嵌套滑动的子View未滑动的距离(未消耗的距离)
     * @param offsetInWindow 子view在当前window的偏移量
     * @return 如果返回true, 表示父view又继续消耗了
     */
    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed,
            dyUnconsumed, offsetInWindow
        )
    }

    /**
     * 子view停止嵌套滑动
     */
    override fun stopNestedScroll() {
        mScrollingChildHelper.stopNestedScroll()
    }

    /**
     * 当子view产生fling滑动时，判断父view是否处拦截fling，如果父View处理了fling，那子view就没有办法处理fling了。
     *
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @return 如果返回true, 表示父view拦截了fling
     */
    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    /**
     * 当父view不拦截子view的fling,那么子view会调用该方法将fling，传给父view进行处理
     *
     * @param velocityX 水平方向上的速度 velocityX > 0  向左滑动，反之向右滑动
     * @param velocityY 竖直方向上的速度 velocityY > 0  向上滑动，反之向下滑动
     * @param consumed  子view是否可以消耗该fling，也可以说是子view是否消耗掉了该fling
     * @return 父view是否消耗了该fling
     */
    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    /**
     * 设置当前子view是否支持嵌套滑动，如果不支持，那么父view是不能够响应嵌套滑动的
     *
     * @param enabled true 支持
     */
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    /**
     * 当前子view是否支持嵌套滑动
     */
    override fun isNestedScrollingEnabled(): Boolean {
        return mScrollingChildHelper.isNestedScrollingEnabled
    }

    /**
     * 判断当前子view是否拥有嵌套滑动的父view
     */
    override fun hasNestedScrollingParent(): Boolean {
        return mScrollingChildHelper.hasNestedScrollingParent()
    }

    private var mLastY = 0
    private var mLastX = 0
    private val mScrollConsumed = IntArray(2)
    private val mScrollOffset = IntArray(2)
    private var mVelocityTracker: VelocityTracker? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val y = event.y.toInt()
        val x = event.x.toInt()

        //添加速度检查器，用于处理fling效果
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x
                mLastY = y
                //自己的处理逻辑,判断传递竖直还是水平方向，这里默认是设置的竖直方向
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                var dy = mLastY - y
                var dx = mLastX - x
                //将事件传递给父控件，并记录父控件消耗的距离
                val ispre = dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset)
                if (ispre) {
                    dx -= mScrollConsumed[0]
                    dy -= mScrollConsumed[1]
                    scrollNested(dx, dy)
                }
                "isPre ${ispre}".logE()
                //如果找不到嵌套滑动的父控件，自己就处理事件
                //childScroll(dx, dy)
            }
            MotionEvent.ACTION_UP -> {
                //当手指抬起的时，结束嵌套滑动传递,并判断是否产生了fling效果
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaxFlingVelocity.toFloat())
                val xvel = mVelocityTracker!!.xVelocity.toInt()
                val yvel = mVelocityTracker!!.yVelocity.toInt()
                fling(xvel, yvel)
                mVelocityTracker!!.clear()
                stopNestedScroll()
            }
            MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll()
                mVelocityTracker!!.clear()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 子控件处理事件，并将未处理完的事件传递给父控件
     *
     * @param x 水平方向移动距离
     * @param y 竖直方向移动距离
     */
    private fun scrollNested(x: Int, y: Int) {
        var unConsumedX = 0
        var unConsumedY = 0
        var consumedX = 0
        var consumedY = 0
        //子控件消耗多少事件，由自己决定
        if (x != 0) {
            consumedX = childConsumeX(x)
            unConsumedX = x - consumedX
        }
        if (y != 0) {
            consumedY = childConsumeY(y)
            unConsumedY = y - consumedY
        }
        //子控件处理事件
        childScroll(consumedX, consumedY)
        //子控件处理后，又将剩下的事件传递给父控件
        if (!dispatchNestedScroll(consumedX, consumedY, unConsumedX, unConsumedY, mScrollOffset)) {
            //传给父控件处理后，剩下的逻辑自己实现
        }
        //传递给父控件，父控件不处理，那么子控件就继续处理。
        childScroll(unConsumedX, unConsumedY)
    }

    /**
     * 子控件滑动逻辑
     */
    private fun childScroll(x: Int, y: Int) {
        //子控件怎么滑动，自己实现
        scrollBy(0, y)
    }

    /**
     * 子控件水平方向消耗多少距离
     */
    private fun childConsumeX(x: Int): Int {
        //具体逻辑由自己实现
        return 0
    }

    /**
     * 子控件竖直方向消耗距离
     */
    private fun childConsumeY(y: Int): Int {
        //具体逻辑由自己实现
        return scrollY
    }

    private fun fling(velocityX: Int, velocityY: Int): Boolean {
        //判断速度是否足够大。如果够大才执行fling
        var velocityX = velocityX
        var velocityY = velocityY
        if (Math.abs(velocityX) < mMinFlingVelocity) {
            velocityX = 0
        }
        if (Math.abs(velocityY) < mMinFlingVelocity) {
            velocityY = 0
        }
        if (velocityX == 0 && velocityY == 0) {
            return false
        }
        if (dispatchNestedPreFling(velocityX.toFloat(), velocityY.toFloat())) {
            val consumed = canScroll()
            //将fling效果传递给父控件
            dispatchNestedFling(velocityX.toFloat(), velocityY.toFloat(), consumed)
            //然后子控件在处理fling效果
            childFling()
        }
        return false
    }

    /**
     * 判断子子控件是否能够滑动，只有能滑动才能处理fling
     */
    private fun canScroll(): Boolean {
        //具体逻辑自己实现
        return false
    }

    /**
     * 子控件处理fling效果
     */
    private fun childFling() {
        //具体逻辑自己实现
    }
}