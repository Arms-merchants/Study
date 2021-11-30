package com.arms.flowview.vp

import android.widget.FrameLayout
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 *    author : heyueyang
 *    time   : 2021/11/26
 *    desc   :
 *    Google给出的解决ViewPage2嵌套ViewPage2的解决方案
 *    因为ViewPage2是一个final的类不允许继承实现
 *    version: 1.0
 */
class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f

    //这里是获取父的ViewPage2一层一层的往上找
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    /**
     * 在childCount大于0的情况下获取第一位的View
     */
    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        //获取一个阀值，在大于这个阀值的情况下才会触发
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    /**
     * canScrollVertically(1)    true能够向上滑动 false 不能向上滑动
     * canScrollVertically(-1)   true能够向下滑动 false 不能向下滑动
     * canScrollHorizontally(1)  true能够向左滑动 false 不能向左滑动
     * canScrollHorizontally(-1) true能够向右滑动 false 不能向左滑动
     */
    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        /**
         * sign的解释 返回参数的符号函数； 如果参数为零，则为零，如果参数大于零，则为 1.0f，如果参数小于零，则为 -1.0f。
         */
        val direction = -delta.sign.toInt()
        //判断在同向时，子view能否继续滑动
        return when (orientation) {
            ORIENTATION_HORIZONTAL -> child?.canScrollHorizontally(direction) ?: false
            ORIENTATION_VERTICAL -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        handleInterceptTouchEvent(e)
        return super.onInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent) {
        //获取父ViewPage2的方向，获取不到那说明没有嵌套ViewPage2直接返回
        val orientation = parentViewPager?.orientation ?: return

        // Early return if child can't scroll in same direction as parent
        //如果子view在相同的方向上已经不能滚动了，那么也不需要处理了
        //1和-1分别测试同轴的两个方向，上和下，左和右对应
        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            //ACTION_DOWN时先把事件拦截掉
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            //ViewPage2是否是横向滑动的
            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            //假设父ViewPage2的滑动阀值是childView的阀值的2倍
            //如果是横向的画x的值取一半
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            //在竖向的话Y的值取一半
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f
            //只有这个值大于阀值的的时候才触发
            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    /**
                     *       也就是横向滑动的话，但是y轴的滑动距离大于x轴，那么也是让父布局拦截
                     *       如果是竖直方向滑动的话，但是x轴滑动的距离大于y轴的，父布局拦截
                     *       也就是只要isVpHorizontal的值等于scaledDy > scaledDx那么就让父布局处理，说明不是需要处理的
                     *       滑动方向
                     */
                    // Gesture is perpendicular, allow all parents to intercept
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    //如果子View能滑动，父布局不拦截事件分发到子view
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        //不能滑动，那么拦截
                        // Child cannot scroll, allow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }
}