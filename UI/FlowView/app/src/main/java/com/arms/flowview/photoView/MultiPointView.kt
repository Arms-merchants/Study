package com.arms.flowview.photoView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.arms.flowview.R
import com.arms.flowview.utils.ConverUtils
import com.arms.flowview.utils.ImageUtils
import com.orhanobut.logger.Logger

/**
 *    author : heyueyang
 *    time   : 2021/12/27
 *    desc   :
 *    version: 1.0
 */
class MultiPointView : View {

    private lateinit var bitmap: Bitmap
    private lateinit var paint: Paint

    // 手指滑动偏移值
    private var offsetX = 0f
    private var offsetY = 0f

    // 按下时的x,y坐标
    private var downX = 0f
    private var downY = 0f

    // 上一次的偏移值
    private var lastOffsetX = 0f
    private var lastOffsetY = 0f

    // 当前按下的pointId
    private var currentPointId = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        init(context)
    }

    private fun init(context: Context?) {
        bitmap =
            ImageUtils.instant.getBitmapBySize(context!!, R.drawable.dm1, ConverUtils.dp2px(300f))
        paint = Paint().apply {
            isAntiAlias = true
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //要想处理多指必须使用actionMasked
        when (event.actionMasked) {
            //DOWN和Up只会触发一次
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                lastOffsetX = offsetX
                lastOffsetY = offsetY
                currentPointId = 0
            }
            MotionEvent.ACTION_MOVE -> {
                //通过id获取当前的下标,这里实现的效果是，在前一根手指
                val index = event.findPointerIndex(currentPointId)

                offsetX = lastOffsetX + event.getX(index) - downX
                offsetY = lastOffsetY + event.getY(index) - downY
                invalidate()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                //多指操作的情况下，从第二个手机落下时会触发
                //获取当前的落下的下标
                val actionIndex = event.actionIndex
                Logger.e("actionIndex:${actionIndex}")
                //通过下标获取对应的手指ID，
                currentPointId = event.getPointerId(actionIndex)
                downX = event.getX(actionIndex)
                downY = event.getY(actionIndex)
                lastOffsetX = offsetX
                lastOffsetY = offsetY
            }
            MotionEvent.ACTION_POINTER_UP -> {
                var upIndex = event.actionIndex
                val pointId = event.getPointerId(upIndex)
                //值处理当前正在相应事件的手指
                if (pointId == currentPointId) {
                    //在手指抬起后获取下一个相应的下标
                    val nextLastIndex =
                        if (upIndex == event.pointerCount - 1) {
                            event.pointerCount - 2
                        } else {
                            upIndex++
                        }
                    currentPointId = event.getPointerId(nextLastIndex)

                    downX = event.getX(nextLastIndex)
                    downY = event.getY(nextLastIndex)
                    lastOffsetX = offsetX
                    lastOffsetY = offsetY
                }

            }

        }

        return true
    }

}