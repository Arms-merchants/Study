package com.arms.flowview.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.arms.flowview.R
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener

import android.view.animation.LinearInterpolator


/**
 *    author : heyueyang
 *    time   : 2021/11/25
 *    desc   : 展示view的应该是分为几个层级，最底层的是应该是传入的照片信息，然后盖一层中间为空心圆的遮罩
 *    在空心圆的外层展示两个圆环的Bitmap
 *    version: 1.0
 */
class FaceView : View {
    private var mDegress = 0f //旋转角度
    private var valueAnimator: ValueAnimator? = null

    private val mPaint = Paint().apply {
        color = Color.WHITE
        this.isAntiAlias = true
        this.isDither = true
    }
    private var innerBitMap: Bitmap? = null
    private var outterBitMap: Bitmap? = null

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, att: AttributeSet) : super(context, att) {

    }

    fun stop() {
        valueAnimator?.pause()
    }

    fun onStart() {
        if(valueAnimator == null){
            valueAnimator = ValueAnimator.ofFloat(0f, 360f)
                .apply {
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    interpolator = LinearInterpolator()
                    duration = 6000
                    addUpdateListener(AnimatorUpdateListener { animation ->
                        mDegress = animation.animatedValue as Float
                        postInvalidate()
                    })
                    start()
                }
        }

        valueAnimator?.apply {
            if (isPaused) {
                this.resume()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        drawCircleMask(canvas)
        drawBitMap(canvas)
    }

    private fun drawCircleMask(canvas: Canvas) {
        canvas.save()
        //先绘制一层白色的遮罩来挡住下面的摄像头展示信息
        canvas.drawRect(Rect(0, 0, width, height), mPaint)
        //设置画笔的属性为CLEAR，相当于就是扣空你再画的图案
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //扣一个圆出来
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 3).toFloat(),
            mPaint
        )
        //把画笔的模式去掉
        mPaint.xfermode = null
        canvas.restore()
    }

    private fun drawBitMap(canvas: Canvas) {
        val dstWidthAndHeight = (width / 1.5f + width / 1.5f / 4).toInt()
        if (innerBitMap == null) {
            innerBitMap =
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_checkface_innercircle
                );
            innerBitMap =
                Bitmap.createScaledBitmap(innerBitMap!!, dstWidthAndHeight, dstWidthAndHeight, true)
        }
        if (outterBitMap == null) {
            outterBitMap =
                BitmapFactory.decodeResource(context.resources, R.drawable.ic_checkface_outcircle);
            outterBitMap =
                Bitmap.createScaledBitmap(
                    outterBitMap!!,
                    dstWidthAndHeight,
                    dstWidthAndHeight,
                    true
                )
        }

        val left = (width - innerBitMap!!.width) / 2
        val top = (height - innerBitMap!!.height) / 2

        canvas.save()
        //内圈正转，那么就是以画布的中心为圆形来进行旋转画布后再绘制内圈
        canvas.rotate(mDegress, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawBitmap(innerBitMap!!, left.toFloat(), top.toFloat(), mPaint)
        canvas.restore()

        canvas.save()
        //外圈反转
        canvas.rotate(-mDegress, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawBitmap(outterBitMap!!, left.toFloat(), top.toFloat(), mPaint)
        canvas.restore()
    }


}