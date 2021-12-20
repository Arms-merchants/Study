package com.arms.flowview.nestedScroll.behavider

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import java.lang.Math.abs

/**
 *    author : heyueyang
 *    time   : 2021/12/20
 *    desc   :
 *    version: 1.0
 */
class TitleBehavior2(context:Context?,attributeSet: AttributeSet?):CoordinatorLayout.Behavior<View>(context,attributeSet) {

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

    var countY = 0

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        countY -= dy
        if(abs(countY)<child.height){
            child.offsetTopAndBottom(countY-child.top)
            consumed[1] = dy
        }
    }
}