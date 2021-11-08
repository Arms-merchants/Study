package com.arms.flowview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/08
 *    desc   :
 *    version: 1.0
 */
public class MyViewPage extends ViewPager {

    public MyViewPage(@NonNull Context context) {
        super(context);
    }

    public MyViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //ViewPage因为是会先测量自己，然后再测量子view，所以它的warp_content不生效，而且ViewPage的高度受限与它的父布局，所以先需要的高度传入 super.onMeasure中就可以
        //不过这个实现的只是同一个高度的情况下的问题，如果要实现每个子布局不同的高度，还需要在切换时重新计算高度
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, layoutParams.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, layoutParams.height);
            view.measure(childWidthSpec, childHeightSpec);
            int h = view.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
