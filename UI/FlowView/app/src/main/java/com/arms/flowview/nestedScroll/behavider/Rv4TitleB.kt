package com.arms.flowview.nestedScroll.behavider

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 *    author : heyueyang
 *    time   : 2021/12/20
 *    desc   :
 *    version: 1.0
 */
class Rv4TitleB(context: Context?,attributeSet: AttributeSet?):CoordinatorLayout.Behavior<View>(context,attributeSet) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is TextView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        child.offsetTopAndBottom(dependency.bottom-child.top)
        return true
    }

}