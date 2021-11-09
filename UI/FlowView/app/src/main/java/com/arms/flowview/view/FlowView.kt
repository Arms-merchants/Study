package com.arms.flowview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/08
 *    desc   :
 *    version: 1.0
 */
public class FlowView extends ViewGroup {

    private int mHorizontalSpacing = dp2px(16); //每个item横向间距
    private int mVerticalSpacing = dp2px(8); //每个item横向间距

    /**
     * 每一行的子view
     */
    private ArrayList<ArrayList<View>> lineViews = new ArrayList<>();
    /**
     * 记录每一行的行高
     */
    private ArrayList<Integer> lineHeight = new ArrayList<>();


    public FlowView(Context context) {
        super(context);
    }

    public FlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //布局的测量是先测量自己还是先测量子view，看需求来定义
        lineViews.clear();
        lineHeight.clear();

        int childCount = getChildCount();
        int height = 0;
        int lineUsedWidth = 0;

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int parentNeededWidth = 0;  // measure过程中，子View要求的父ViewGroup的宽
        int parentNeededHeight = 0; // measure过程中，子View要求的父ViewGroup的高

        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = child.getLayoutParams();
            if (child.getVisibility() != View.GONE) {

                //MeasureSpec 构成包括两个部分，mode和size，也就是一个view是宽是什么的测量模式，它的宽度是多少
                //一个view的大小是要受限制于它的父布局的，getChildMeasureSpec所以在这个方法中的第一个参数是父布局的MeasureSpec,如果父布局也不能确认这个
                //的来源于父布局的父布局或者更往上。
                //MeasureSpec 是一个32为的Int，它的低2位用来确认测量模式，高30位来存放size， 01，00，11

                //进行子view的测量
                int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), layoutParams.width);
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), layoutParams.height);

                measureChild(child, childWidthMeasureSpec, childHeightMeasureSpec);

                int childHeight = child.getMeasuredHeight();
                int childWidth = child.getMeasuredWidth();

                //根据逻辑进行判断相应的逻辑换行
                if (childWidth + lineUsedWidth + mHorizontalSpacing > maxWidth) {
                    //换行
                    lineViews.add(views);
                    lineHeight.add(height);

                    parentNeededHeight = childHeight + parentNeededHeight + mVerticalSpacing;
                    parentNeededWidth = Math.max(parentNeededWidth, lineUsedWidth + mHorizontalSpacing);

                    views = new ArrayList<>();
                    lineUsedWidth = 0;
                    height = 0;
                }
                lineUsedWidth = lineUsedWidth + childWidth + mHorizontalSpacing;
                height = Math.max(height, childHeight);
                views.add(child);

                //处理最后一行，最后一行的宽度是不会达到最大宽度的
                if (i == childCount - 1) {
                    lineViews.add(views);
                    lineHeight.add(height);
                    parentNeededHeight = parentNeededHeight + height + mVerticalSpacing;
                    parentNeededWidth = Math.max(parentNeededWidth, lineUsedWidth + mHorizontalSpacing);
                }
            }
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //如果当前的布局给定了固定的宽高，那么直接用给的宽高而不是测量子view算出的宽高
        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? maxWidth : parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? maxHeight : parentNeededHeight;
        setMeasuredDimension(realWidth, realHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineCount = lineHeight.size();
        int curL = getPaddingLeft();
        int curT = getPaddingTop();

        for (int i = 0; i < lineCount; i++) {
            ArrayList<View> lineView = lineViews.get(i);
            int height = lineHeight.get(i);
            for (int y = 0; y < lineView.size(); y++) {
                View view = lineView.get(y);
                int left = curL;
                int top = curT;
                int right = curL + view.getMeasuredWidth();
                int bottom = curT + view.getMeasuredHeight();
                view.layout(left, top, right, bottom);
                curL = right + mHorizontalSpacing;
            }
            curT = curT + height + mVerticalSpacing;
            curL = getPaddingLeft();
        }
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
