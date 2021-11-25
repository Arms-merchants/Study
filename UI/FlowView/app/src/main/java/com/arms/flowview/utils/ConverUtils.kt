package com.arms.flowview.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by heyueyang on 2021/11/17
 */
object ConverUtils {


    /**
     * 方法传入[dp]来进行相应的dp转px的操作
     */
    fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}