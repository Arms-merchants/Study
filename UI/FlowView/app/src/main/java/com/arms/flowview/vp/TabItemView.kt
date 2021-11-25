package com.arms.flowview.vp

import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 *    author : heyueyang
 *    time   : 2021/11/25
 *    desc   :
 *    version: 1.0
 */
class TabItemView : View {

    var mPaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.BLUE
    }

    var scale: Float = 2f;

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        init()
    }

    private fun init() {
        ValueAnimator.ofFloat(2f, 0f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()
            duration = 6000
            addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
                scale = animation.animatedValue as Float
                postInvalidate()
            })
            start()
        }
    }


    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas == null) {
            return
        }
        val y = 200f
        val x = 200f
        canvas.drawCircle(x, y, 100f, mPaint)
        canvas.drawCircle(3 * x, y, 100f, mPaint)

        val path = Path()
        path.moveTo(x, y - 100f)
        path.quadTo(2 * x * scale, 190f, 3 * x, 100f)
        path.lineTo(3 * x, 300f)
        path.quadTo(2 * x * scale, 210f, x, 300f)

        canvas.drawPath(path, mPaint)
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
    fun getPoint(startPoint: PointF, findLength: Float, angle: Float): PointF? {
        val point = PointF()
        //角度转为弧度，因为在Math的sin和cos需要的弧度
        val deltaX = (Math.cos(Math.toRadians(angle.toDouble())) * findLength).toFloat()
        //这里因为android的坐标轴和数学坐标轴在y轴方向是反的，所以需要取反或者-180度
        val deltaY = (-(Math.sin(Math.toRadians(angle.toDouble())) * findLength)).toFloat()
        point[startPoint.x + deltaX] = startPoint.y + deltaY
        return point
    }


}