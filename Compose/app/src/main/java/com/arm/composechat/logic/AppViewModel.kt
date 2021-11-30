package com.arm.composechat.logic

import androidx.lifecycle.ViewModel
import com.arm.composechat.cache.AppThemeCache
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */
class AppViewModel : ViewModel() {

    val appTheme = MutableStateFlow(AppThemeCache.currentTheme)

    fun switchToNextTheme() {
        val nextTheme = appTheme.value.nextTheme()
        AppThemeCache.onAppThemeChanged(nextTheme)
        appTheme.value = nextTheme
    }
}