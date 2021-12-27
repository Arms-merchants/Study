package com.arms.flowview.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import com.arms.flowview.R

/**
 *    author : heyueyang
 *    time   : 2021/12/27
 *    desc   :
 *    version: 1.0
 */
class ImageUtils {

    companion object {
        val instant: ImageUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            ImageUtils()
        }
    }

    fun getBitmapBySize(context: Context, @DrawableRes id: Int, size: Int): Bitmap {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, id, option)
        option.inJustDecodeBounds = false
        option.inDensity = option.outWidth
        option.inTargetDensity = size
        return BitmapFactory.decodeResource(context.resources, id, option)
    }

}