package com.arms.flowview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.arms.flowview.R
import com.arms.flowview.utils.ConverUtils
import com.arms.flowview.utils.ScreenUtils
import kotlin.math.cos
import kotlin.math.sin

/**
 *    author : heyueyang
 *    time   : 2021/12/08
 *    desc   :
 *
 *    旋转木马，360度平分为8个，
 * https://mp.weixin.qq.com/s?__biz=MzIxMTg5NjQyMA==&mid=2247505040&idx=1&sn=ea4e8f6eb96d6527d9b409001870fddf&chksm=974cc59ba03b4c8d33aaf8db2cecb366d91092336a43dc12d3f3eb9b142adc3842f8b7fd19e3&mpshare=1&scene=1&srcid=1208QFVEvoQke5tnrymKzqyq&sharer_sharetime=1638928139756&sharer_shareid=7603858e092ada181333dc2a1cdb984d&version=3.1.20.90367&platform=mac#rd
 *    version: 1.0
 */
class MuMaView : View {

    private var valueAnimator: ValueAnimator? = null
    private var mDegress = 0f //旋转角度
    private lateinit var muMaBitmap: Bitmap;

    //以屏幕的中心作为整个视图的中心
    private val centerX = ScreenUtils.getScreenWidth() / 2
    private val centerY = ScreenUtils.getScreenHeight() / 2

    //中心点坐标
    private val centerPointF = PointF(centerX.toFloat(), centerY.toFloat())


    private val paint: Paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = ConverUtils.dp2px(2f).toFloat()
    }

    val RADIUS = ScreenUtils.getScreenWidth() / 2 - ConverUtils.dp2px(50f)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    private fun init() {
        valueAnimator = ValueAnimator.ofFloat(0f, 360f)
            .apply {
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
                duration = 10000
                addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
                    mDegress = animation.animatedValue as Float
                    postInvalidate()
                })
                start()
            }
        muMaBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.muma2)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        //循环绘制子view
        for (i in 0..360 step 45) {
            if (i == 360) {
                break
            }
            //加入一个mDegress来实现循环动起来的效果，通过ValueAnimator来进行控制
            val itemAngel = i - mDegress

            /**
             * 参考链接不过这个是compose的
             *
             */
            //缩放比例因为cos【0-90】正好是一个1到0的范围区间，所以缩放的范围就是minScale到1
            var scale =
                (1 - minScale) / 2 * (1 + cos(Math.toRadians(itemAngel.toDouble() + 90))) + minScale
            val itemCenterPointF = getPoint(centerPointF, RADIUS.toFloat(), itemAngel)
            val itemWidth = (200 * scale).toInt();
            //绘制，不过有问题，其中y的点是通过强制转换的，会导致整个图像的上下移动，做不到固定中心
            //还是计算有问题。。。
            val itemRect = Rect()
            //TODO 待解决的两个问题，1。y的计算公式需要明确（现在的是一个乱写的凑合能用）2。视图层级的问题
            //最开始是绘制圆形的图像，所以想要实现立体效果，那么就要将整体图像沿着x进行旋转，所以x的坐标不会有变化
            //需要变化的是y的坐标
            //而且因为绘制层级的问题会导致重叠的部分会出现视图的穿透，也就是底部视图遮挡到了前面的视图
            val itemY =
                itemCenterPointF.y * cos(30f) - itemWidth / 2 + ScreenUtils.getScreenHeight() / 2
            //确定要
            itemRect.top = (itemY - itemWidth / 2).toInt()
            itemRect.left = (itemCenterPointF.x - itemWidth / 2).toInt()
            itemRect.right = (itemCenterPointF.x + itemWidth / 2).toInt()
            itemRect.bottom = (itemY + itemWidth / 2).toInt()
            canvas.drawBitmap(muMaBitmap, null, itemRect, paint)
            /* canvas.drawCircle(
                 itemCenterPointF.x,
                 itemCenterPointF.y * cos(30f),
                 30f * scale.toFloat(),
                 paint
             )*/
        }
    }

    private val minScale = 0.4f

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
    private fun getPoint(startPoint: PointF, findLength: Float, angle: Float): PointF {
        val point = PointF()
        //角度转为弧度，因为在Math的sin和cos需要的弧度
        val deltaX = (cos(Math.toRadians(angle.toDouble())) * findLength).toFloat()
        //这里因为android的坐标轴和数学坐标轴在y轴方向是反的，所以需要取反或者-180度
        val deltaY = (-(sin(Math.toRadians(angle.toDouble())) * findLength)).toFloat()
        point[startPoint.x + deltaX] = startPoint.y + deltaY
        return point
    }


}