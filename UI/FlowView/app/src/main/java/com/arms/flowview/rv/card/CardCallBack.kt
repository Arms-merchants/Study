package com.arms.flowview.rv.card

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.rv.StarAdapter
import com.arms.flowview.rv.StarBean
import kotlin.math.sqrt

/**
 *    author : heyueyang
 *    time   : 2021/11/23
 *    desc   :
 *    version: 1.0
 */
class CardCallBack(
    val mData: MutableList<Any>,
    val swipCallback: OnSwipCallback,
    val adapter: StarAdapter
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    /**
     * 这是和拖拽相关
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    /**
     * 滑动
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //这里就滑动之后给到的回调用
        val remove: Any? = mData.remove(viewHolder.layoutPosition)
        remove?.let {
            mData.add(0, it)
        }
        adapter.notifyDataSetChanged()
        //swipCallback.onSwiped(mData)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        //先根据滑动的dxdy 算出现在动画的比例系数fraction
        //先根据滑动的dxdy 算出现在动画的比例系数fraction
        val maxDistance = recyclerView.width*0.5f
        val swipValue = sqrt((dX * dX + dY * dY).toDouble())
        var fraction: Double = swipValue / maxDistance
        //边界修正 最大为1
        //边界修正 最大为1
        if (fraction > 1) {
            fraction = 1.0
        }
        //对每个ChildView进行缩放 位移
        //对每个ChildView进行缩放 位移
        val childCount = recyclerView.childCount
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            //第几层,举例子，count =7， 最后一个TopView（6）是第0层，
            val level = childCount - i - 1
            if (level > 0) {

                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    child.scaleX =
                        (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP).toFloat()
                    child.scaleY =
                        (1 - CardConfig.SCALE_GAP * level + fraction * CardConfig.SCALE_GAP).toFloat()
                    child.translationY =
                        (CardConfig.TRANS_Y_GAP * level - fraction * CardConfig.TRANS_Y_GAP).toFloat()
                } /*else {
                    child.translationY = (CardConfig.TRANS_Y_GAP * (level - 1) - fraction * CardConfig.TRANS_Y_GAP).toFloat();
                }*/
            }
        }

    }

    interface OnSwipCallback {
        fun onSwiped(data: List<*>?)
    }


}