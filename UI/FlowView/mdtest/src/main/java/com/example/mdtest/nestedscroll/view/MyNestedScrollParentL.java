package com.example.mdtest.nestedscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

public class MyNestedScrollParentL extends LinearLayout implements NestedScrollingParent {

    private static String Tag = "Zero";

    private NestedScrollingParentHelper helper;

    public MyNestedScrollParentL(Context context) {
        super(context);
        init(context);
    }

    public MyNestedScrollParentL(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyNestedScrollParentL(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MyNestedScrollParentL(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        helper = new NestedScrollingParentHelper(this);
    }

    int realHeight = 0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        realHeight = 0;
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        for(int i = 0; i < getChildCount(); i++){
            View view = getChildAt(i);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),MeasureSpec.UNSPECIFIED);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            realHeight += view.getMeasuredHeight();
        }
        Log.i("parent: onMeasure","realHeight: " + realHeight);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     * @param child
     * @param target
     * @param nestedScrollAxes ????????????????????????????????????????????????X???????????????Y??????????????????????????????????????????true???false
     * @return ??????false??????????????????
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i(Tag, "onStartNestedScroll--" + "child:" + child + ",target:" + target + ",nestedScrollAxes:" + nestedScrollAxes);
        return true;
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.i(Tag, "onNestedScrollAccepted" + "child:" + child + ",target:" + target + ",nestedScrollAxes:" + nestedScrollAxes);
        helper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.i(Tag, "onStopNestedScroll--target:" + target);
        helper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(Tag, "onNestedScroll--" + "target:" + target + ",dxConsumed" + dxConsumed + ",dyConsumed:" + dyConsumed
                + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" + dyUnconsumed);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean show = showImg(dy);
        boolean hide = hideImg(dy);
//        Log.i("onMeasure","show: " + show + ", hide: " + hide);
        if(show||hide){
            consumed[1] = dy;//????????????
            scrollBy(0, dy );
            Log.i(Tag,"Parent?????????"+dy);
        }
        Log.i(Tag, "onNestedPreScroll--getScrollY():" + getScrollY() + ",dx:" + dx + ",dy:" + dy + ",consumed:" + consumed[1]);
    }

    private boolean showImg(int dy){
        View view =getChildAt(0);
//        Log.i("onMeasure","showImg: " + dy + " height: " + view.getHeight() + " getScrollY: " + getScrollY() + " can: " + view.canScrollVertically(-1));
        if(dy <0 && getScrollY() >0 && !view.canScrollVertically(-1)){
            return true;
        }
        return false;
    }

    private boolean hideImg(int dy){
        View view =getChildAt(0);
//        Log.i("onMeasure","showImg: " + dy + " height: " + view.getHeight() + " getScrollY: " + getScrollY() + " can: " + view.canScrollVertically(-1));
        if(dy > 0 && getScrollY() < view.getHeight()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(Tag, "onNestedFling--target:" + target);
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i(Tag, "onNestedPreFling--target:" + target);
        return true;
    }

    @Override
    public void scrollTo(int x, int y) {
        View view =getChildAt(0);
        if (y < 0)
        {
            y = 0;
        }
        if (y > view.getHeight())
        {
            y = view.getHeight();
        }
        if (y != getScrollY())
        {
            super.scrollTo(x, y);
        }
    }
}
