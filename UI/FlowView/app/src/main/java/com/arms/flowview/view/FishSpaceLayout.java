package com.arms.flowview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/13
 *    desc   :
 *    version: 1.0
 */
public class FishSpaceLayout extends RelativeLayout {

    private Paint mPaint;
    private int level = 0;
    private float touchX, touchY;
    /**
     * 水波纹的最大半径
     */
    private float maxRaduis = 0;

    private FishDrawable mFishDrawable;
    private ImageView ivFish;


    public FishSpaceLayout(Context context) {
        super(context);
        init(context);
    }


    public FishSpaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FishSpaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        mFishDrawable = new FishDrawable();
        ivFish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(layoutParams);
        ivFish.setImageDrawable(mFishDrawable);
        addView(ivFish);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touchX = event.getX();
            touchY = event.getY();
            ObjectAnimator.ofInt(this, "level", 0, 100).setDuration(1000).start();

            PointF fishMiddlePoint = mFishDrawable.getMiddlePoint();
            PointF fishHeadPoint = mFishDrawable.getHeadCenterPoint();
            PointF realyFishMiddlePoint = new PointF(ivFish.getX() + fishMiddlePoint.x, ivFish.getY() + fishMiddlePoint.y);
            PointF realyFishHeadPoint = new PointF(ivFish.getX() + fishHeadPoint.x, ivFish.getY() + fishHeadPoint.y);
            PointF touchPoint = new PointF(touchX, touchY);

            /**
             * 这里获取的是根据三个点的坐标获得O的夹角 AOT(鱼头，鱼重心，触摸点的构成的夹角坐标)
             */
            float angle = getRemoveAngle(realyFishMiddlePoint, realyFishHeadPoint, touchPoint) / 2;
            //第二点就是c点的一个点，为了求角度的值，
            float delta = getRemoveAngle(realyFishMiddlePoint, new PointF(realyFishMiddlePoint.x + 1, realyFishMiddlePoint.y), realyFishHeadPoint);
            //控制二点的坐标
            PointF controlPoint = mFishDrawable.getPoint(realyFishMiddlePoint, mFishDrawable.HEAD_RADIUS * 1.6f, angle + delta);

            Path path = new Path();
            path.moveTo(realyFishMiddlePoint.x - fishMiddlePoint.x, realyFishMiddlePoint.y - fishMiddlePoint.y);
            path.cubicTo(realyFishHeadPoint.x - fishMiddlePoint.x, realyFishHeadPoint.y - fishMiddlePoint.y,
                    controlPoint.x - fishMiddlePoint.x, controlPoint.y - fishMiddlePoint.y,
                    touchPoint.x - fishMiddlePoint.x, touchPoint.y - fishMiddlePoint.y);

            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivFish, "x", "y", path);
            objectAnimator.setDuration(2000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mFishDrawable.setFrequence(2.0f);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mFishDrawable.setFrequence(1.0f);
                }
            });

            PathMeasure pathMeasure = new PathMeasure(path, false);
            float[] tan = new float[2];
            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = animation.getAnimatedFraction();
                    pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                    float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                    mFishDrawable.setFishMainAngle(angle);
                }
            });
            objectAnimator.start();
        }
        return super.onTouchEvent(event);
    }


    /**
     * 通过三个点获取其中给的夹角
     *
     * @param O
     * @param A
     * @param B
     * @return
     */
    public float getRemoveAngle(PointF O, PointF A, PointF B) {

        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        float OA = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        float OB = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));

        float cosAOB = AOB / (OA * OB);
        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));
        //需要判断当前触摸点在OA中线的位置
        //AB连线与X的夹角的tan值-OB与x轴夹角的tan值
        //根据这个值就能判断出当前触摸点是在鱼的那边
        float direction = (A.y - B.y) / (A.x - B.x) - (O.x - B.x) / (O.y - B.y);
        if (direction == 0) {
            //相当于就是鱼头朝上或者朝下
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        int alpha = (int) (100 * (1 - level / 100f));
        mPaint.setAlpha(alpha);
        maxRaduis = level;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(touchX, touchY, maxRaduis, mPaint);

    }
}
