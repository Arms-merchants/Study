package com.arm.composechat

import android.app.Application
import com.arm.composechat.cache.AppThemeCache
import com.arm.composechat.model.AppTheme
import com.arm.composechat.ui.weigets.CoilImageLoader
import com.arm.composechat.utils.ContextHolder

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */
class ChatApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        ContextHolder.init(this)
        CoilImageLoader.init(this)

        AppThemeCache.init()
    }
}