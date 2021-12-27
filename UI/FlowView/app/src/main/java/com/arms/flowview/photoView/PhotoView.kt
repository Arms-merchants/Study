package com.arms.flowview.photoView

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.OverScroller
import com.arms.flowview.R
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.ConverUtils
import com.arms.flowview.utils.ImageUtils

/**
 *    author : heyueyang
 *    time   : 2021/12/21
 *    desc   :还有两地需要优化，处理最开始图片加载的问，最开始图片加载的大小不对，图片资源的获取现在是直接构建死的本地图片路径，这里如果要支持url的话要怎么处理
 *    version: 1.0
 */
class PhotoView : View {


    private lateinit var bitmap: Bitmap
    private val mPaint = Paint()

    /**
     * 图像最初始时的x和y的坐标
     */
    var originalOffsetX = 0f
    var originalOffsetY = 0f

    //图像的偏移坐标
    private var offSetX = 0f
    private var offSetY = 0f

    /**
     * 放大的比例
     */
    private val OVER_SCALE_FACTOR = 1.5f

    //当前的缩放比例
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }

    //最小的缩放比例
    private var smallScale = 0f

    //最大的缩放比例
    private var bigScale = 0f

    //记录当前的状态是不是已经是放大
    private var isEnlarge = false

    /**
     * 使用提供的MotionEvent检测各种手势和事件。 GestureDetector.OnGestureListener回调将在发生特定运动事件时通知用户。 此类应仅与通过触摸报告的MotionEvent一起使用（不要用于轨迹球事件）。 要使用这个类：
    为您的View创建一个GestureDetector实例
    在View.onTouchEvent(MotionEvent)方法中，确保您调用onTouchEvent(MotionEvent) 。 回调中定义的方法将在事件发生时执行。
    如果监听GestureDetector.OnContextClickListener.onContextClick(MotionEvent)你必须在View.onGenericMotionEvent(MotionEvent)onGenericMotionEvent(MotionEvent)
    中调用onGenericMotionEvent(MotionEvent) View.onGenericMotionEvent(MotionEvent) 。
     */
    private lateinit var gestureDetector: GestureDetector

    /**
     * 此类封装了滚动，并具有超出滚动操作边界的能力。 在大多数情况下，此类是Scroller替代品
     */
    private lateinit var overScroller: OverScroller

    private lateinit var scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context?) {
        //获取bitmap
        bitmap =
            ImageUtils.instant.getBitmapBySize(context!!, R.drawable.test, ConverUtils.dp2px(300f))
        //注册手势监听类
        gestureDetector = GestureDetector(context, gestureDetectorListener)
        //为了实现在滑动到边缘的回弹效果
        overScroller = OverScroller(context)
        //缩放操作
        scaleGestureDetector = ScaleGestureDetector(context, PhotoScaleGestureListener())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //currentScale当前的比例是不断变化的，这样计算获取到当前需要变化的比例，例如最开始的适合currentScale = smallScale，那么scaleFaction = 0，translate的操作就是（0，0）不需要偏移
        val scaleFaction = (currentScale - smallScale) / (bigScale - smallScale)
        "scaleFaction:${scaleFaction}----currentScale:${currentScale}----smallScale:${smallScale}----bigScale:${bigScale}".logE()
        //画布偏移
        canvas.translate(offSetX * scaleFaction, offSetY * scaleFaction)
        //根据比例以控件中心为轴心进行缩放
        canvas.scale(currentScale, currentScale, width / 2f, height / 2f)
        //绘制图像
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, mPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        originalOffsetX = (width - bitmap.width) / 2f
        originalOffsetY = (height - bitmap.height) / 2f
        //放大的比例，最大的放大比例取宽高中小的那个，因为它的比例大
        if ((bitmap.width / bitmap.height) > width / height) {
            //说明是宽大于高的
            smallScale = (width / bitmap.width).toFloat()
            bigScale = (height / bitmap.height).toFloat() * OVER_SCALE_FACTOR
        } else {
            smallScale = (height / bitmap.height).toFloat()
            bigScale = (width / bitmap.width).toFloat() * OVER_SCALE_FACTOR
        }
        currentScale = smallScale
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var result = scaleGestureDetector.onTouchEvent(event)
        if (!scaleGestureDetector.isInProgress) {
            result = gestureDetector.onTouchEvent(event)
        }
        return result
    }

    private var scaleAnimator: ObjectAnimator? = null

    private fun getScaleAnimation(smallScale: Float, bigScale: Float): ObjectAnimator {
        if (scaleAnimator == null) {
            //缩放处理的为currentScale这个属性，范围为smallScale到bigScale
            scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0f)
        }
        scaleAnimator!!.setFloatValues(smallScale, bigScale)
        return scaleAnimator!!
    }

    /**
     * 控制边界范围
     */
    private fun fixOffsets() {
        //x轴的偏移必须在一个范围之内，这个范围的最大值是大图的最大宽度-控件宽度的一半，这里是在图像的右边，而在左边是的是负值的范围，y轴类似
        offSetX = Math.min(offSetX, (bitmap.width * bigScale - width) / 2f)
        offSetX = Math.max(offSetX, -(bitmap.width * bigScale - width) / 2f)
        offSetY = Math.min(offSetY, (bitmap.height * bigScale - height) / 2f)
        offSetY = Math.max(offSetY, -(bitmap.height * bigScale - height) / 2f)
    }


    /**
     * 手势处理类
     */
    val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

        /**
         * 一个事件的开始是从down开始的，如果down没有获取到
         * 那么后续的事件也不会再消费，所以这里为了实现双击的效果，需要消费事件
         */
        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        /**
         * 显示水波纹
         */
        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
        }

        /**
         * 单击事件的，up事件时回掉
         */
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return super.onSingleTapUp(e)
        }

        /**
         * 滑动是进行图像移动
         */
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            //只在大图的情况下进行触发
            if (isEnlarge) {
                offSetX -= distanceX
                offSetY -= distanceY
                fixOffsets()
                invalidate()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        /**
         * 长按
         */
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
        }

        /**
         * 惯性滑动，在惯性滑动的情况下实现一个回弹的效果
         */
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (isEnlarge) {
                //overX – 超出范围。 如果 > 0，则可以在任一方向进行水平翻转。
                //overY – 超出范围。 如果 > 0，则可以在任一方向进行垂直翻转
                overScroller.fling(
                    offSetX.toInt(), offSetY.toInt(), velocityX.toInt(), velocityY.toInt(),
                    (-(bitmap.width * bigScale - width) / 2).toInt(),
                    ((bitmap.width * bigScale - width) / 2).toInt(),
                    (-(bitmap.height * bigScale - height) / 2).toInt(),
                    ((bitmap.height * bigScale - height) / 2).toInt(), 300, 300
                )
                postOnAnimation(FlishRunner())
            }
            return super.onFling(e1, e2, velocityX, velocityY)
        }

        /**
         * 点击在up以及Tag时会触发回掉
         */
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return super.onSingleTapConfirmed(e)
        }

        /**
         * 双击事件的的Event回掉，包括有down，move，up
         */
        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            return super.onDoubleTapEvent(e)

        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            return super.onContextClick(e)
        }

        /**
         * 双击事件的第二次down事件会进行回掉
         */
        override fun onDoubleTap(e: MotionEvent): Boolean {
            isEnlarge = !isEnlarge
            if (isEnlarge) {
                //实现的效果是点击是点击什么地方放大什么地方，所以需要对坐标进行偏移处理
                offSetX = (e.x - width / 2) - (e.x - width / 2) * bigScale / smallScale
                offSetY = (e.y - height / 2) - (e.y - height / 2) * bigScale / smallScale
                fixOffsets()
                //通过动画来进行缩放处理
                getScaleAnimation(smallScale, bigScale).start()
            } else {
                //动画反转
                val tempScale = Math.min(currentScale, bigScale)
                getScaleAnimation(smallScale, tempScale).reverse()
            }
            return super.onDoubleTap(e)
        }
    }

    inner class FlishRunner : Runnable {
        override fun run() {
            //computeScrollOffset为true，说明动画还没结束
            if (overScroller.computeScrollOffset()) {
                //currX获取的是当原点和现在位置的绝对距离，也就是我们需要的偏移量，因为需要回弹处理，这里不需要处理边界问题
                offSetX = overScroller.currX.toFloat()
                offSetY = overScroller.currY.toFloat()
                invalidate()
                postOnAnimation(this)
            }
        }
    }

    /**
     * 手势发生时接收通知的监听器。 如果你想监听所有不同的手势，那么实现这个接口。 如果您只想侦听子集，
     * 扩展ScaleGestureDetector.SimpleOnScaleGestureListener可能会更容易。 应用程序将按以下顺序接收事件：
    一个onScaleBegin(ScaleGestureDetector)
    零个或多个onScale(ScaleGestureDetector)
    一个onScaleEnd(ScaleGestureDetector)
     */
    inner class PhotoScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {

        var initScale = 0f;

        /**
         * 响应正在进行的手势的缩放事件。 通过指针运动报告
         * @param
         * @return 检测器是否应将此事件视为已处理。 如果事件未被处理，探测器将继续累积运动，直到事件被处理。 例如，如果应用程序只想在更改大于 0.01 时更新缩放因子，这会很有用。
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            currentScale = initScale * detector.scaleFactor
            isEnlarge = currentScale > smallScale
            invalidate()
            return false
        }

        /**
         * @return 检测器是否应继续识别此手势。 例如，如果手势开始于有意义的区域之外的焦点，则 onScaleBegin() 可能会返回 false 以忽略手势的其余部分
         */
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            //这是缩放比例不都是从零开始的
            initScale = currentScale
            return true
        }

        /**
         * 手势缩放结束时，处理边界问题
         */
        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            if (currentScale > bigScale) {
                getScaleAnimation(bigScale, currentScale).reverse()
            } else if (currentScale < smallScale) {
                getScaleAnimation(currentScale, smallScale).start()
            }
        }

    }


}