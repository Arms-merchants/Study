package com.arms.flowview.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * <pre>
 * author : heyueyang
 * time   : 2021/11/08
 * desc   :
 * version: 1.0
</pre> */
class FlowView : ViewGroup {
    private val mHorizontalSpacing = dp2px(16) //每个item横向间距
    private val mVerticalSpacing = dp2px(8) //每个item横向间距

    /**
     * 每一行的子view
     */
    private val lineViews = ArrayList<ArrayList<View>>()

    /**
     * 记录每一行的行高
     */
    private val lineHeight = ArrayList<Int>()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //布局的测量是先测量自己还是先测量子view，看需求来定义
        lineViews.clear()
        lineHeight.clear()
        val childCount = childCount
        var height = 0
        var lineUsedWidth = 0
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec)
        var parentNeededWidth = 0 // measure过程中，子View要求的父ViewGroup的宽
        var parentNeededHeight = 0 // measure过程中，子View要求的父ViewGroup的高
        var views = arrayListOf<View>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val layoutParams = child.layoutParams
            if (child.visibility != GONE) {

                //MeasureSpec 构成包括两个部分，mode和size，也就是一个view是宽是什么的测量模式，它的宽度是多少
                //一个view的大小是要受限制于它的父布局的，getChildMeasureSpec所以在这个方法中的第一个参数是父布局的MeasureSpec,如果父布局也不能确认这个
                //的来源于父布局的父布局或者更往上。
                //MeasureSpec 是一个32为的Int，它的低2位用来确认测量模式，高30位来存放size， 01，00，11

                //进行子view的测量
                val childWidthMeasureSpec = getChildMeasureSpec(
                    widthMeasureSpec,
                    paddingLeft + paddingRight,
                    layoutParams.width
                )
                val childHeightMeasureSpec = getChildMeasureSpec(
                    heightMeasureSpec,
                    paddingTop + paddingBottom,
                    layoutParams.height
                )
                measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec)
                val childHeight = child.measuredHeight
                val childWidth = child.measuredWidth

                //根据逻辑进行判断相应的逻辑换行
                if (childWidth + lineUsedWidth + mHorizontalSpacing > maxWidth) {
                    //换行
                    lineViews.add(views)
                    lineHeight.add(height)
                    parentNeededHeight += childHeight + mVerticalSpacing
                    parentNeededWidth =
                        parentNeededWidth.coerceAtLeast(lineUsedWidth + mHorizontalSpacing)
                    views = ArrayList()
                    lineUsedWidth = 0
                    height = 0
                }
                lineUsedWidth += childWidth + mHorizontalSpacing
                height = height.coerceAtLeast(childHeight)
                views.add(child)

                //处理最后一行，最后一行的宽度是不会达到最大宽度的
                if (i == childCount - 1) {
                    lineViews.add(views)
                    lineHeight.add(height)
                    parentNeededHeight += height + mVerticalSpacing
                    parentNeededWidth =
                        parentNeededWidth.coerceAtLeast(lineUsedWidth + mHorizontalSpacing)
                }
            }
        }
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //如果当前的布局给定了固定的宽高，那么直接用给的宽高而不是测量子view算出的宽高
        val realWidth = if (widthMode == MeasureSpec.EXACTLY) maxWidth else parentNeededWidth
        val realHeight =
            if (heightMode == MeasureSpec.EXACTLY) maxHeight else parentNeededHeight + paddingTop + paddingBottom
        setMeasuredDimension(realWidth, realHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val lineCount = lineHeight.size
        var curL = paddingLeft
        var curT = paddingTop
        for (i in 0 until lineCount) {
            val lineView = lineViews[i]
            val height = lineHeight[i]
            for (y in lineView.indices) {
                val view = lineView[y]
                val left = curL
                val top = curT
                val right = curL + view.measuredWidth
                val bottom = curT + view.measuredHeight
                view.layout(left, top, right, bottom)
                curL = right + mHorizontalSpacing
            }
            curT += height + mVerticalSpacing
            curL = paddingLeft
        }
    }

    companion object {
        fun dp2px(dp: Int): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                Resources.getSystem().displayMetrics
            ).toInt()
        }
    }
}