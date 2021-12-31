package com.arms.flowview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   : 通过画布裁剪进行文本变色的绘制效果，其中mPercent代表当前的比例，实现原理，绘制两层的text设置不同的
 *    文本颜色，但是通过clipRect裁剪显示区域，并根据进度做刷新从而实现了根据进度变色的效果。
 *    version: 1.0
 */
public class ColorChangeTextView extends AppCompatTextView {

    private Paint mPain;


    private float mPercent = 0.0f;

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float mPercent) {
        this.mPercent = mPercent;
        invalidate();//重绘
    }

    public ColorChangeTextView(Context context) {
        super(context);
    }

    public ColorChangeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorChangeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLine(canvas);
        mPain = new Paint();
        mPain.setColor(Color.BLACK);
        mPain.setAntiAlias(true);
        mPain.setTextSize(80);
        mPain.setStyle(Paint.Style.FILL);
        /*
        //最大的区间范围
        public float   top;
        public float   bottom;
        //正常文字的区间范围，是不是可以理解为我们常用的字体除去多余padding的操作之后的值
        public float   ascent;
        public float   descent;
        */
        drawCentText(canvas);
        drawCentText1(canvas);
    }

    private void drawCentText(Canvas canvas) {
        canvas.save();
        mPain.setColor(Color.BLACK);
        Paint.FontMetrics fontMetrics = mPain.getFontMetrics();
        String text = getText().toString();
        float textWidth = mPain.measureText(text);
        float x = getWidth() / 2 - textWidth / 2;
        float baseline = getHeight() / 2 - (fontMetrics.descent + fontMetrics.ascent) / 2;
        float left = x + textWidth * mPercent;
        Rect rect = new Rect((int) left, 0, getWidth(), getHeight());
        canvas.clipRect(rect);
        canvas.drawText(text, x, baseline, mPain);
        canvas.restore();
    }

    private void drawCentText1(Canvas canvas) {
        canvas.save();
        mPain.setColor(Color.BLUE);
        String text = getText().toString();
        //这里是为了获取文本的绘制基线的
        Paint.FontMetrics fontMetrics = mPain.getFontMetrics();
        //获取文本的宽度
        float textWidth = mPain.measureText(text);
        float x = getWidth() / 2 - textWidth / 2;
        float baseline = getHeight() / 2 - (fontMetrics.descent + fontMetrics.ascent) / 2;
        //获取裁剪的矩形区域代表有多大的区域要绘制为蓝色的文字字体
        float left = getWidth() / 2 - textWidth / 2;
        float right = left + textWidth * mPercent;

        Rect rect = new Rect((int) left, 0, (int) right, getHeight());
        canvas.clipRect(rect);
        canvas.drawText(text, x, baseline, mPain);
        canvas.restore();
    }


    private void drawLine(Canvas canvas) {
        Paint linePaint = new Paint();
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(3f);
        linePaint.setStyle(Paint.Style.FILL);

        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, linePaint);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), linePaint);
    }


}
