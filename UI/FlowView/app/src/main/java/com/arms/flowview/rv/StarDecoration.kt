package com.arms.flowview.rv

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.utils.ConverUtils




/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    //实力在recycleview的添加分组标题，并实现悬停效果
 *    1。在确定需要添加分组头部的地方预留出足够的空间，通过getItemOffsets ，outRect设置相应的空间距离
 *    2。onDraw 和onDrawOver的区别，item的绘制顺序会遵循onDraw-》item draw-》onDrawOver，也就是item的绘制内容会遮挡
 *    onDraw绘制的内容，而onDrawOver绘制的内容又会在item draw的上方，所以如果是没有遮挡需求的情况下一般的绘制在onDraw中完成就好
 *    3。悬停标题的绘制，获取在屏幕上展示的view数量（recyclerview.childCount）,判断当前是否有头部需要进行标题的绘制
 *     标题绘制的区域坐标为
 *     itemView 当前需要展示标题的子View
 *     left = recyclerview.paddingLeft
 *     right = recyclerview.width - recyclerview.paddingRight
 *     bottom = itemView.top
 *     top = itemView.top-HeadHeight//因为是往上，所以是负的坐标
 *     标题文本的绘制
 *     绘制文本的范围 Paint.getTextBounds("",start,end,Rect)
 *     Rect就会存储有文本的width，height等信息
 *     文本绘制的坐标
 *     x = left
 *     y = top + HeadHeight/2+rect.height/2
 *     到这里分组标题已经绘制完成了
 *    4。绘制悬停的标题
 *      因为悬停的标题会遮挡item view，所以绘制是在onDrawOver中进行的
 *      绘制的区域坐标
 *
 *    version: 1.0
 */
class StarDecoration() : RecyclerView.ItemDecoration() {

    private val mTextPaint by lazy {
        Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            isDither = true
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }
    }
    private val mHeadPaint by lazy {
        val rect = Rect()
        mTextPaint.getTextBounds("111",0,2,rect)

        Paint().apply {
            color = Color.RED
        }
    }

    private var rect: Rect = Rect()
    private val headSize = ConverUtils.dp2px(30f)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val adapter = parent.adapter as StarAdapter
        //childCount是屏幕上显示的总数量
        for (index in 0 until parent.childCount) {
            val itemView = parent.getChildAt(index)
            //这里的坐标是对不上的,这里的index是在屏幕上显示的第几个的位置，需要转为itemview在整体列表当中的位置
            val position = parent.getChildLayoutPosition(itemView)
            val isGroupHead = adapter.isGroupHead(position)
            if (isGroupHead&& itemView.top-headSize-parent.paddingTop>=0) {
                c.drawRect(
                    left.toFloat(),
                    (itemView.top - headSize).toFloat(), right.toFloat(),
                    itemView.top.toFloat(), mHeadPaint
                )
                val groupName = adapter.data?.get(position)?.groupName
                mTextPaint.getTextBounds(groupName, 0, groupName!!.length, rect)
                c.drawText(
                    groupName, left.toFloat(),
                    (itemView.top - headSize / 2 + rect.height() / 2).toFloat(), mTextPaint)
            } else if(itemView.top-headSize-parent.paddingTop>=0) {
                c.drawRect(
                    left.toFloat(),
                    (itemView.top - ConverUtils.dp2px(1f)).toFloat(),
                    right.toFloat(), itemView.top.toFloat(), mHeadPaint
                )
            }

        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val startAdapter = parent.adapter as StarAdapter
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val top = parent.paddingTop
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val itemPosition = layoutManager.findFirstVisibleItemPosition()
        val groupName = startAdapter.data?.get(itemPosition)?.groupName
        /**
         * 如果item的高度远远小于head高度，那么就会有问题，所以覆盖的view的bottom应该是由下一个head的top决定的
         * 怎么去找下个head？
         * 只有下一个head的top等于当前覆盖的bottom时，才会进行上移的绘制，不然一直就是在固定位置的绘制
         */
        var isStartUp = false
        var headBottom = top+headSize
        for(i in 0 until parent.childCount){
            val itemView = parent.getChildAt(i)
            //这里的坐标是对不上的,这里的index是在屏幕上显示的第几个的位置，需要转为itemview在整体列表当中的位置
            val position = parent.getChildLayoutPosition(itemView)
            val isGroupHead =  startAdapter.isGroupHead(position)
            //是否开上移操作的要求，第一个是一个group的头
            //第二不是下标0的元素
            //第三下个head的top应该开小于等于当前展示的head的bottom
            //第四head的移动距离别是在headsize-0的范围呢，也就是GroupHead的ItemView的top - headSize - RecyclveView.paddingTOP要大于等于0
            isStartUp =isGroupHead&&itemView.top-headSize<top+headSize &&itemView.top-headSize-top>=0 && position!= 0
            if(isStartUp){
                //当有一个符合条件的时候就可以跳出循环，避免有个headView在同一屏幕下的问题
                headBottom = itemView.top-headSize
                break
            }
        }

        if(isStartUp){
            c.drawRect(left.toFloat(),
                top.toFloat(),right.toFloat(),
                headBottom.toFloat(),mHeadPaint)
            mTextPaint.getTextBounds(groupName, 0, groupName!!.length, rect)
            val textRect = Rect(left,top,right,top+headSize)
            c.clipRect(textRect)
            c.drawText(
                groupName, left.toFloat(),
                (headBottom - headSize / 2 + rect.height() / 2).toFloat(), mTextPaint)
        }else{
            //如果不是头部标题的话就在最顶部绘制一个固定的标题
            c.drawRect(
                left.toFloat(),
                top.toFloat(), right.toFloat(),
                (top+headSize).toFloat(), mHeadPaint
            )
            mTextPaint.getTextBounds(groupName, 0, groupName!!.length, rect)
            c.drawText(
                groupName, left.toFloat(),
                (top + headSize / 2 + rect.height() / 2).toFloat(), mTextPaint)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemViewPosition = parent.getChildAdapterPosition(view)
        val startAdapter = parent.adapter as StarAdapter
        if (startAdapter.isGroupHead(itemViewPosition)) {
            outRect.set(0, headSize, 0, 0)
        } else {
            outRect.set(0, ConverUtils.dp2px(1f), 0, 0)
        }
    }

}