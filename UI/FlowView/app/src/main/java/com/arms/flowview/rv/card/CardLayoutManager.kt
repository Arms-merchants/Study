package com.arms.flowview.rv.card

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *    author : heyueyang
 *    time   : 2021/11/23
 *    desc   :
 *    version: 1.0
 */
class CardLayoutManager : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * 确定子view如何进行布局
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        //首先进行布局的回收
        if (recycler == null) {
            return
        }
        //这里的回收复用参考LinearLayoutManager的调用，回收复用就是通过这个实现的
        detachAndScrapAttachedViews(recycler)
        //如果没有子View那么也没有必要进行
        if (itemCount < 1) {
            return
        }
        //确定最后一个展示的view
        val bottomPosition = if (itemCount < CardConfig.MAX_SHOW_COUNT) {
            0
        } else {
            itemCount - CardConfig.MAX_SHOW_COUNT
        }

        for (i in bottomPosition until itemCount) {
            //给子view进行测量以及居中操作
            val itemView = recycler.getViewForPosition(i)
            addView(itemView)

            measureChildWithMargins(itemView, 0, 0)
            val widthSpace = width - getDecoratedMeasuredWidth(itemView)
            val heightSpace = height - getDecoratedMeasuredHeight(itemView)

            // 布局 ---draw -- onDraw ,onDrawOver, onLayout
            layoutDecoratedWithMargins(
                itemView, widthSpace / 2, heightSpace / 2,
                widthSpace / 2 + getDecoratedMeasuredWidth(itemView),
                heightSpace / 2 + getDecoratedMeasuredHeight(itemView)
            )

            //根据层级缩放，越往上的层级缩放级别越大
            //因为整个是基于整个数据集合进行缩放展示的，这里有一个最大展示数量的概念，所以当到达最大展示数量的时候后面的view跟最后一级一致就可以了
            val level = itemCount - i - 1
            if (level > 0) {
                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    itemView.translationY = (CardConfig.TRANS_Y_GAP * level).toFloat()
                    itemView.scaleX = 1 - CardConfig.SCALE_GAP * level
                    itemView.scaleY = 1 - CardConfig.SCALE_GAP * level
                } else {
                    itemView.translationY = (CardConfig.TRANS_Y_GAP * (level - 1)).toFloat()
                    itemView.scaleY = 1 - CardConfig.SCALE_GAP * (level - 1)
                    itemView.scaleX = 1 - CardConfig.SCALE_GAP * (level - 1)
                }
            }
        }
    }

}