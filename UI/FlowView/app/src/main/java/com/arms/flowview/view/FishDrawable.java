package com.arms.flowview.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

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
    private PointF headCenterPoint;
    // 鱼的主要朝向角度
    private float fishMainAngle = 90;

    private float frequence = 1f;

    private float yuqiFrequence = 1f;

    /**
     * 鱼的长度值
     */
    // 绘制鱼头的半径,也是影响整个鱼的大小尺寸的要素，因为整体的鱼的尺寸都是以鱼头为基准的
    public float HEAD_RADIUS = 40;
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

    /**
     * ------动画属性
     */
    private final float CHANGE_VALUE = 3600f;
    private final int ANIMATOR_DURATION = 8 * 1000;

    private float currentValue = 0f;

    public FishDrawable() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);
        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, CHANGE_VALUE);
        valueAnimator.setDuration(ANIMATOR_DURATION);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimator.start();

        startFish();
    }

    private ValueAnimator yuqiAnimator;

    public void startFish() {
        yuqiAnimator = ValueAnimator.ofFloat(1f, 0.7f);
        yuqiAnimator.setDuration(700);
        yuqiAnimator.setRepeatCount(ValueAnimator.INFINITE);
        yuqiAnimator.setRepeatMode(ValueAnimator.REVERSE);
        yuqiAnimator.setInterpolator(new LinearInterpolator());

        yuqiAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                yuqiFrequence = (float) animation.getAnimatedValue();
            }
        });
        yuqiAnimator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentValue * 1.2 * frequence)) * 4);

        //鱼头
        headCenterPoint = getPoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headCenterPoint.x, headCenterPoint.y, HEAD_RADIUS, mPaint);
        //右鱼鳍
        PointF rightStartPoint = getPoint(headCenterPoint, FIND_FINS_LENGTH, fishAngle - 110);
        drawYuQi(canvas, rightStartPoint, fishAngle, true);
        //左鱼鳍
        PointF leftStartPoint = getPoint(headCenterPoint, FIND_FINS_LENGTH, fishAngle + 110);
        drawYuQi(canvas, leftStartPoint, fishAngle, false);
        //下节一
        //下节一的点是身体的下点中心
        PointF bodyBottomCentPoint = getPoint(middlePoint, BODY_LENGTH / 2, fishAngle - 180);
        PointF nextCentPoint = makeSegment(canvas, bodyBottomCentPoint, BIG_CIRCLE_RADIUS, MIDDLE_CIRCLE_RADIUS, FIND_MIDDLE_CIRCLE_LENGTH, fishAngle, true);
        makeSegment(canvas, nextCentPoint, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS, FIND_SMALL_CIRCLE_LENGTH, fishAngle, false);

        //尾巴
        makeTriangel(canvas, nextCentPoint, FIND_TRIANGLE_LENGTH, BIG_CIRCLE_RADIUS, fishAngle);
        makeTriangel(canvas, nextCentPoint, FIND_TRIANGLE_LENGTH - 10, BIG_CIRCLE_RADIUS - 20, fishAngle);

        makeBody(canvas, headCenterPoint, bodyBottomCentPoint, fishAngle);
    }

    /**
     * 绘制身体
     *
     * @param canvas
     * @param headPoint
     * @param bodyBottomCenterPoint
     * @param fishAngle
     */
    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBottomCenterPoint, float fishAngle) {
        //身体的四个点
        PointF leftTopPoint = getPoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF leftBottomPoint = getPoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF rightTopPoint = getPoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle + 90);
        PointF rightBottomPoint = getPoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle - 90);

        // 二阶贝塞尔曲线的控制点 --- 决定鱼的胖瘦
        PointF controlLeft = getPoint(headPoint, BODY_LENGTH * 0.56f,
                fishAngle + 130);
        PointF controlRight = getPoint(headPoint, BODY_LENGTH * 0.56f,
                fishAngle - 130);

        mPath.reset();
        mPath.moveTo(leftTopPoint.x, leftTopPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, rightTopPoint.x, rightTopPoint.y);
        mPath.lineTo(rightBottomPoint.x, rightBottomPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, leftBottomPoint.x, leftBottomPoint.y);
        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeTriangel(Canvas canvas, PointF startPoint, float findBottomCentLength, float findEdgeLength, float fishAngle) {

        float trangelAngle = (float) (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5f * frequence)) * 35);

        //获取三角形的底边中心
        PointF bottomCenterPoint = getPoint(startPoint, findBottomCentLength, trangelAngle - 180);

        //获取三角形的另外两个点
        PointF leftPoint = getPoint(bottomCenterPoint, findEdgeLength, trangelAngle + 90);
        PointF rightPoint = getPoint(bottomCenterPoint, findEdgeLength, trangelAngle - 90);

        //点连线画三角
        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);
        canvas.drawPath(mPath, mPaint);
    }


    private PointF makeSegment(Canvas canvas, PointF bottomCenterPoint, float bigRadius, float smallRadius,
                               float findSmallCircleLength, float fishAngle, boolean hasBigCircle) {
        float trangelAngle;
        if (hasBigCircle) {
            trangelAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5f * frequence)) * 15);
        } else {
            trangelAngle = (float) (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5f * frequence)) * 35);
        }

        PointF topCentPoint = getPoint(bottomCenterPoint, findSmallCircleLength, trangelAngle - 180);

        PointF leftTopPoint = getPoint(bottomCenterPoint, bigRadius, trangelAngle + 90);
        PointF leftBottomPoint = getPoint(bottomCenterPoint, bigRadius, trangelAngle - 90);
        PointF rightTopPoint = getPoint(topCentPoint, smallRadius, trangelAngle + 90);
        PointF rightBottomPoint = getPoint(topCentPoint, smallRadius, trangelAngle - 90);
        if (hasBigCircle) {
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, mPaint);
        }
        canvas.drawCircle(topCentPoint.x, topCentPoint.y, smallRadius, mPaint);
        mPath.reset();
        mPath.moveTo(leftTopPoint.x, leftTopPoint.y);
        mPath.lineTo(rightTopPoint.x, rightTopPoint.y);
        mPath.lineTo(rightBottomPoint.x, rightBottomPoint.y);
        mPath.lineTo(leftBottomPoint.x, leftBottomPoint.y);
        mPath.lineTo(leftTopPoint.x, leftTopPoint.y);
        canvas.drawPath(mPath, mPaint);
        return topCentPoint;
    }


    private void drawYuQi(Canvas canvas, PointF startPoint, float fishAngle, boolean isRight) {
        PointF rightEndPoint = getPoint(startPoint, FINS_LENGTH, fishAngle - 180);
        PointF controlPoint = getPoint(startPoint, 1.8f * FINS_LENGTH * yuqiFrequence,
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
    public PointF getPoint(PointF startPoint, float findLength, float angle) {
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

    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(PointF middlePoint) {
        this.middlePoint = middlePoint;
    }

    public PointF getHeadCenterPoint() {
        return headCenterPoint;
    }

    public void setHeadCenterPoint(PointF headCenterPoint) {
        this.headCenterPoint = headCenterPoint;
    }

    public void setFrequence(float frequence) {
        this.frequence = frequence;
    }

    public void setFishMainAngle(float fishMainAngle) {
        this.fishMainAngle = fishMainAngle;
    }

    public void setYuqiFrequence(float yuqiFrequence) {
        this.yuqiFrequence = yuqiFrequence;
    }
}
