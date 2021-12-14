package com.arms.flowview.drag

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.orhanobut.logger.Logger

/**
 *    author : heyueyang
 *    time   : 2021/12/14
 *    desc   : 内部view关联Drag视图，实现点击关闭视图的效果
 *    version: 1.0
 */
class InnerView : LinearLayout {

    private val mDragView: MyDragView
        get() {
            return parent as MyDragView
        }


    constructor(context: Context?) : super(context) {

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (mDragView.status !== DragStatus.Close) {
            Logger.e("onInterceptTouchEvent")
            true
        } else super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mDragView.status !== DragStatus.Close) {
            if (event.action == MotionEvent.ACTION_UP) {
                mDragView.close()
            }
            Logger.e("inner onTouchEvent")
            return true
        }
        return super.onTouchEvent(event)
    }


}