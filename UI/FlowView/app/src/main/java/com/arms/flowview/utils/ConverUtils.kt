package com.arms.flowview.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created by heyueyang on 2021/11/17
 */
object ConverUtils{

    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}