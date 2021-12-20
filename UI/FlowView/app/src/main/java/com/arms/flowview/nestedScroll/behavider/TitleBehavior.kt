package com.arms.flowview.nestedScroll.behavider

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 *    author : heyueyang
 *    time   : 2021/12/20
 *    desc   :
 *    version: 1.0
 */
class TitleBehavior(context:Context?, attributeSet: AttributeSet?):CoordinatorLayout.Behavior<View>(context,attributeSet) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    /**
     * 已经发生的偏移量
     */
    var mOffsetTopAndBottom =0

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        //滑动中消耗的Y轴的距离
        var consumedy = 0
        //获取在已经发生的偏移量基础上加上当前滑动距离
        var offset = mOffsetTopAndBottom-dy
        //最小的滑动范围，应为是向上收起所以定义为当前view的负值
        val minOffset = -child.height
        //最大的滑动范围
        val maxOffset = 0
        //根据区间获取偏移量
        offset = if(offset<minOffset){
            minOffset
        }else if(offset > maxOffset){
            maxOffset
        }else{
            offset
        }
       //offset中是总的偏移量，所以需要将已经移动的距离也就是view的top减掉
        child.offsetTopAndBottom(offset-child.top)
        consumedy = mOffsetTopAndBottom - offset
        mOffsetTopAndBottom = offset
        //消耗的Y轴的量
        consumed[1] = consumedy
    }
}