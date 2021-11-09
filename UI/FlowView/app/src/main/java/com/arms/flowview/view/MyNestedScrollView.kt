package com.arms.flowview.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.utils.FlingHelper

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   : 嵌套滑动，并带有惯性滑动的处理，还包含一种在ScrollView实现置顶效果的实现
 *    version: 1.0
 */
class MyNestedScrollView:NestedScrollView {

    private lateinit var mContentView:ViewGroup
    private lateinit var mHeadView:ViewGroup
    private lateinit var mFlingHelper: FlingHelper
    private var isStartFling = false
    private var velocityY = 0
    private var totalDy = 0

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context:Context):super(context){
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet){
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init(){
        mFlingHelper = FlingHelper(context)
        setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(isStartFling){
                velocityY = 0
                isStartFling = false
            }
            if(scrollY ==0 ){
                //这里就是ScrollView到顶了
                Log.e("=====","TOP")
            }
           val bottomHeight =  (getChildAt(0).measuredHeight - v.measuredHeight)
            val headViewHeight = mHeadView.measuredHeight
            Log.e("=====","scroll:${scrollY}")
            if(scrollY == headViewHeight){
                Log.e("====","其实就是滑倒底了${scrollY}")
            }
            if(scrollY == (getChildAt(0).measuredHeight - v.measuredHeight)){
                //因为我们是给内容布局设置高度为ScrollView的高度
                Log.e("=====","Bottom")
                //滑动到底之后其实要把剩余的需要滑动的距离交给子View去处理
                dispatchChildFling()
            }
            totalDy+=scrollY-oldScrollY
        }
    }

    private fun dispatchChildFling(){
        if(velocityY != 0){
            val splineFlingDistance = mFlingHelper.getSplineFlingDistance(velocityY)
            //这是通过速度获取到的滑动距离，跟scrollview的滑动距离做判断，如果已经全部被消费了，那么就不需要再往下分配了
            if(splineFlingDistance > totalDy){
                //剩余的滑动距离再转为速度给到子view去处理
                childFling(mFlingHelper.getVelocityByDistance(splineFlingDistance - totalDy))
            }
        }
    }

    private fun childFling( velY:Int){
      val rv =   mContentView.getChildAt(1) as RecyclerView
        rv.fling(0,velocityY)
    }

    override fun fling(velocityY: Int) {
        //接受飞速滑动的速度，有速度说明滑动开始了
        super.fling(velocityY)
        if(velocityY<=0){
            this.velocityY = 0
        }else{
            isStartFling = true
            this.velocityY = velocityY
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val layoutParams =  mContentView.layoutParams
        layoutParams.height = measuredHeight
        mContentView.layoutParams = layoutParams
    }

    /**
     * 这里就是嵌套滑动中父布局在子view滑动前先滑动多少距离，滑动的距离通过consumed返回
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     * @param type
     */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val hideTop = dy >0 && scrollY<mHeadView.measuredHeight
        if(hideTop){
            scrollBy(0,dy)
            consumed[1] =dy
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        //这个方法先调用。。。
        mContentView = (getChildAt(0)as ViewGroup).getChildAt(1) as ViewGroup
        mHeadView = (getChildAt(0)as ViewGroup).getChildAt(0) as ViewGroup
    }

}