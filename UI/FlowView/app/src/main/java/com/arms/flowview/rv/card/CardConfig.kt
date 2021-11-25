package com.arms.flowview.rv.card

import android.content.ContentUris
import android.content.Context
import android.util.TypedValue
import com.arms.flowview.utils.ConverUtils


/**
 *    author : heyueyang
 *    time   : 2021/11/23
 *    desc   :
 *    version: 1.0
 */
object CardConfig {

        /**
         * 屏幕上最多同时显示几个Item，这个地方有个bug，最大显示数层的最下面一层会和它上面一层的偏移会是一个效果
         * 所以显示的效果是max -1，所以要显示3的话那么max需要比设置显示多1
         */
        var MAX_SHOW_COUNT = 0

        /**
         * 每一级Scale相差0.05f，translationY相差7dp左右
         */
        var SCALE_GAP = 0f
        var TRANS_Y_GAP = 0
        var TRANS_Z_GAP = 0

    fun initConfig() {
        MAX_SHOW_COUNT = 4
        SCALE_GAP = 0.08f
        TRANS_Y_GAP = ConverUtils.dp2px(20f)
        TRANS_Z_GAP = ConverUtils.dp2px(0.5f)
    }

}