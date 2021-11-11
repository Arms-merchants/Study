package com.arms.flowview.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/10
 *    desc   :
 *    version: 1.0
 */
public class FishDrawable extends Drawable {

    private Paint mPaint;
    private Path mPath;

    private int OTHER_ALPHA = 110;
    private int BODY_ALPHA = 160;

    // 鱼的重心
    private PointF middlePoint;
    // 鱼的主要朝向角度
    private float fishMainAngle = 90;

    /**
     * 鱼的长度值
     */
    // 绘制鱼头的半径
    private float HEAD_RADIUS = 100;
    // 鱼身长度
    private float BODY_LENGTH = HEAD_RADIUS * 3.2f;
    // 寻找鱼鳍起始点坐标的线长
    private float FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS;
    // 鱼鳍的长度
    private float FINS_LENGTH = 1.3f * HEAD_RADIUS;
    // 大圆的半径
    private float BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS;
    // 中圆的半径
    private float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;
    // 小圆半径
    private float SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS;
    // --寻找尾部中圆圆心的线长
    private final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1);
    // --寻找尾部小圆圆心的线长
    private final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    // --寻找大三角形底边中心点的线长
    private final float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;


    public FishDrawable() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);
        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

        float fishAngle = fishMainAngle;

        //鱼头
        PointF headCenterPoint = getPoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headCenterPoint.x, headCenterPoint.y, HEAD_RADIUS, mPaint);

        PointF rightStartPoint = getPoint(headCenterPoint, FIND_FINS_LENGTH, fishAngle - 110);
        drawYuQi(canvas, rightStartPoint, fishAngle, true);

        PointF leftStartPoint = getPoint(headCenterPoint, FIND_FINS_LENGTH, fishAngle + 110);
        drawYuQi(canvas, leftStartPoint, fishAngle, false);

    }


    private void drawYuQi(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        PointF rightEndPoint = getPoint(startPoint, FINS_LENGTH, fishAngle - 180);
        PointF controlPoint = getPoint(startPoint, 1.8f * FINS_LENGTH,
                isRight ? fishAngle - 115 : fishAngle + 115);
        mPath.reset();
        //二阶贝塞尔曲线，起点，终点，控制点
        mPath.moveTo(startPoint.x, startPoint.y);
        //控制点的坐标，结束点的坐标
        mPath.quadTo(controlPoint.x, controlPoint.y, rightEndPoint.x, rightEndPoint.y);
        canvas.drawPath(mPath, mPaint);
    }


    /**
     * 三角函数,通过一个点还有它的弧度以及斜边长，获取他的点的坐标
     * x = cosAngle * r
     * y = sinAngle * r
     *
     * @param startPoint
     * @param findLength
     * @param angle
     * @return
     */
    private PointF getPoint(PointF startPoint, float findLength, float angle) {
        PointF point = new PointF();
        //角度转为弧度，因为在Math的sin和cos需要的弧度
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * findLength);
        //这里因为android的坐标轴和数学坐标轴在y轴方向是反的，所以需要取反或者-180度
        float deltaY = (float) -(Math.sin(Math.toRadians(angle)) * findLength);
        point.set(startPoint.x + deltaX, startPoint.y + deltaY);
        return point;
    }


    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }
}
