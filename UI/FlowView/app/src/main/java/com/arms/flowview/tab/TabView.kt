package com.arms.flowview.tab

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.Scroller
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ScreenUtils
import com.orhanobut.logger.Logger
import kotlin.math.abs

/**
 *    author : Bio
 *    time   : 2021/12/10
 *    desc   :
 *    1。测量子view的总的宽然后想家这样就获得到了具体的宽
 *     2。测量获取子view中最高的高度作为view的高
 *    version: 1.0
 */
class TabView : ViewGroup {
    /**
     * 视图需要的实际宽高
     */
    private var realyWidth = 0
    private var realyHeight = 0

    /**
     * 最小的滑动距离
     */
    private var mMinTouchSlop = 0

    /**
     * 右边界
     */
    private var rightBorder = 0

    private var mScroller: Scroller

    private var mVelocityTracker: VelocityTracker? = null

    private var mMaximumVelocity = 0
    private var mMinimumVelocity = 0


    constructor(context: Context) : super(context) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val viewConfiguration = ViewConfiguration.get(context)
        mMinTouchSlop = viewConfiguration.scaledTouchSlop
        mScroller = Scroller(context)
        mMaximumVelocity = viewConfiguration.scaledMaximumFlingVelocity
        mMinimumVelocity = viewConfiguration.scaledMinimumFlingVelocity
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        realyWidth = 0
        realyHeight = 0
        rightBorder = 0
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility == View.GONE) {
                continue
            }
            val layoutParams = childView.layoutParams as MarginLayoutParams
            val childMeasureWidthSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight,
                layoutParams.width
            )

            val childMeasureHeightSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom,
                layoutParams.height
            )
            measureChild(childView, childMeasureWidthSpec, childMeasureHeightSpec)
            val childW =
                childView.measuredWidth + layoutParams.marginStart + layoutParams.marginEnd
            val childH =
                childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
            realyWidth += childW
            realyHeight = childH.coerceAtLeast(realyHeight)
        }
        //如果布局中指定了控件的高，那么使用这个明确的高作为这个
        if (heightMode == MeasureSpec.EXACTLY) {
            realyHeight = heightSize
        } else {
            //处理添加padding的问题
            realyHeight += paddingTop + paddingBottom
        }
        val screenWidth = ScreenUtils.getScreenWidth()
        rightBorder = if (realyWidth < screenWidth) {
            0
        } else {
            realyWidth - screenWidth + paddingLeft
        }
        setMeasuredDimension(realyWidth, realyHeight)

    }

    /**
     * 设置MarginLayoutParams不然在转换使用的时候会报错
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = paddingTop
        var left = paddingLeft
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            val layoutParams = childView.layoutParams as MarginLayoutParams
            val clL = left + layoutParams.marginStart
            //让内部的子view垂直方向居中显示
            val childNeedHeight =
                childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + paddingBottom + paddingTop
            val clT = if (childNeedHeight < realyHeight) {
                (realyHeight - childNeedHeight) / 2 + paddingTop
            } else {
                top + layoutParams.topMargin
            }
            val clR = clL + childView.measuredWidth + layoutParams.marginEnd
            val clB = clT + childView.measuredHeight
            left += childView.measuredWidth + layoutParams.marginStart + layoutParams.marginEnd
            childView.layout(clL, clT, clR, clB)
        }
    }

    private var lastX = 0
    private var mMoveX = 0
    private var mCurrentScrollX = 0

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        Logger.e("onInterceptHoverEvent")
        if (event != null) {
            val action = event.actionMasked
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.x.toInt()
                    mMoveX = event.x.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastX
                    if (abs(dx) >= mMinTouchSlop) {
                        return true
                    }
                    lastX = event.x.toInt()
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain()
            }
            mVelocityTracker!!.addMovement(event)
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    val dx = mMoveX - event.x
                    if (scrollX + dx <= 0) {
                        scrollTo(0, 0)
                        return true
                    }
                    Logger.e("scrollX:${scrollX} == realyWidth:${realyWidth}")
                    if (scrollX + dx >= rightBorder) {
                        scrollTo(rightBorder, 0)
                        return true
                    }
                    scrollBy(dx.toInt(), 0)
                    mMoveX = event.x.toInt()
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    mVelocityTracker?.apply {
                        computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                        val velocityX = xVelocity
                        Logger.e("velocityX:${velocityX}=====mMinimumVelocity:${mMinimumVelocity}")
                        if (abs(velocityX) >= mMinimumVelocity) {
                            mCurrentScrollX = scrollX
                            mScroller.fling(
                                mCurrentScrollX,
                                0,
                                velocityX.toInt(),
                                0,
                                0,
                                width,
                                0,
                                0
                            )
                        }
                        recycle()
                    }
                    mVelocityTracker = null
                }

            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        super.computeScroll()
        //判断是否完成了滑动
        if (mScroller.computeScrollOffset()) {
            //mScroller.currX返回滚动中的当前 X 偏移量。
            var dx = mCurrentScrollX - mScroller.currX
            //几个参数的意义，mCurrentScrollX在UP时的位置，mCurrentScrollX - mScroller.currX获取到的就是要滑动的距离，如果当前的x加上dx大于最大的范围
            //那么就是最大范围，如果当前x加上dx（可以为负）小于0了那么就是0，剩余的就是滑动这个dx
            "mCurrentScrollX:${mCurrentScrollX}======dx:${dx}====scrollx:${scrollX}-------rightBorder:${rightBorder}".logE()
            if (scrollX + dx >= rightBorder) {
                dx = scrollX + dx - rightBorder
                scrollTo(rightBorder, 0)
            } else if (scrollX + dx < 0) {
                scrollTo(0, 0)
            } else {
                scrollBy(dx, 0)
            }
            postInvalidate()
        }

    }


}