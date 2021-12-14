package com.arms.flowview.drag

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import java.lang.Math.abs


/**
 *    author : heyueyang
 *    time   : 2021/12/14
 *    desc   : 实现一个包含有侧滑显示出底部菜单的布局视图
 *    version: 1.0
 */
class MyDragView : ViewGroup {
    //菜单布局和内容视图布局
    private lateinit var mContentView: View
    private lateinit var mMenuView: View

    /**
     * 是一个用于编写自定义 ViewGroup 的实用程序类。 它提供了许多有用的操作和状态跟踪，允许用户在其父 ViewGroup 中拖动和重新定位视图。
     */
    private lateinit var mViewDragHelper: ViewDragHelper

    /**
     * 使用提供的MotionEvent检测各种手势和事件。 GestureDetector.OnGestureListener回调将在发生特定运动事件时通知用户。 此类应仅与通过触摸报告的MotionEvent一起使用（不要用于轨迹球事件）。
    框架的 GestureDetector 的这种兼容性实现保证了 Jellybean MR1 在所有平台版本上的更新焦点滚动行为。
    要使用这个类：
    为您的View创建一个GestureDetectorCompat实例
    在View.onTouchEvent(MotionEvent)方法中，确保您调用onTouchEvent(MotionEvent) 。 回调中定义的方法将在事件发生时执行。
     */
    private lateinit var mGestureDetector: GestureDetectorCompat

    /**
     * 菜单视图的宽度
     */
    private var mMenuViewWidth = 0

    /**
     * 当前的状态
     */
    var status = DragStatus.Close
        get() {
            when (mContentView.left) {
                0 -> {
                    DragStatus.Close
                }
                mMenuViewWidth -> {
                    DragStatus.Open
                }
                else -> {
                    DragStatus.Drag
                }
            }.also { field = it }
            return field
        }
        private set

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    private fun init() {
        mViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            /**
             * 判断是不是可以拖拽的view
             */
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child == mContentView
            }

            /**
             * 如果为0那么这个view就不能拖拽，通过这个方法限定拖拽的范围
             */
            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return when {
                    mContentView.left + dx < 0 -> {
                        0
                    }
                    mContentView.left + dx > mMenuViewWidth -> {
                        mMenuViewWidth
                    }
                    else -> {
                        left
                    }
                }
            }

            /**
             * 这个默认值返回的是0，这是造成无法横向滑动的原因，这里应该给出横向滑动范围
             */
            override fun getViewHorizontalDragRange(child: View): Int {
                return mMenuViewWidth
            }

            /**
             * 当子视图不再被主动拖动时调用。 如果相关，还会提供抛掷速度。 速度值可以被限制为系统最小值或最大值。
             *
            ReleasedChild – 捕获的子视图现在正在释放
            xvel – 指针离开屏幕时的 X 速度（以像素/秒为单位）。
            yvel – 指针离开屏幕时的 Y 速度（以每秒像素为单位）。
             */
            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                when {
                    //限定当内容布局的左边距达到菜单视图一半以上时为打开状态
                    mContentView.left > mMenuViewWidth / 2 -> {
                        open()
                    }
                    else -> {
                        close()
                    }
                }
            }
        })
        mGestureDetector = GestureDetectorCompat(context, YScrollDetector())
    }

    /**
     * 手势判断，这里是滑动时x轴的距离要大于y轴的距离
     */
    internal inner class YScrollDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return abs(distanceY) <= abs(distanceX)
        }
    }

    /**
     * 布局加载完成获取内容视图和菜单视图
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        mMenuView = getChildAt(0)
        mContentView = getChildAt(1)
        /*mMenuView.isClickable = true
        mContentView.isClickable = true*/
    }

    /**
     * 获取菜单视图的宽度
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mMenuViewWidth = mMenuView.measuredWidth
    }

    /**
     * 设置布局
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //菜单是在内容层的下面
        mMenuView.layout(0, 0, mMenuView.measuredWidth, mMenuView.measuredHeight)
        mContentView.layout(0, 0, mContentView.measuredWidth, mContentView.measuredHeight)
    }

    /**
     * 通过helper和手势来进行拦截事件的判断，固定写法
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val dragB = mViewDragHelper.shouldInterceptTouchEvent(ev)
        val gestB = mGestureDetector.onTouchEvent(ev)
        return dragB && gestB
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        //固定写法
        mViewDragHelper.processTouchEvent(e)
        return false
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var needWidth = 0
        var needHeight = 0
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val layoutParams = childView.layoutParams
            val childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, layoutParams.width)
            val childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, layoutParams.height)
            childView.measure(childWidthSpec, childHeightSpec)
            needWidth += childView.measuredWidth
            needHeight = needHeight.coerceAtLeast(childView.measuredHeight)
        }
        setMeasuredDimension(needWidth, needHeight)
    }

    fun open() {
        if (mViewDragHelper.smoothSlideViewTo(mContentView, mMenuViewWidth, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun close() {
        if (mViewDragHelper.smoothSlideViewTo(mContentView, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }
}