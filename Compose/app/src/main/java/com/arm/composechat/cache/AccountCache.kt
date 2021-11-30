package com.arm.composechat.cache

import android.content.Context
import com.arm.composechat.utils.ContextHolder

/**
 *    author : heyueyang
 *    time   : 2021/11/30
 *    desc   :
 *    version: 1.0
 */
object AccountCache {

    private const val KEY_GROUP = "AccountGroup"

    private const val KEY_LAST_LOGIN_USER_ID = "keyLastLoginUserId"

    private const val KEY_AUTO_LOGIN = "keyAutoLogin"

    private val preferences by lazy {
        ContextHolder.context.getSharedPreferences(KEY_GROUP, Context.MODE_PRIVATE)
    }

    val lastLoginUserId: String
        get() = preferences.getString(KEY_LAST_LOGIN_USER_ID, "") ?: ""

    val canAutoLogin: Boolean
        get() = preferences.getBoolean(KEY_AUTO_LOGIN, true)

    fun onUserLogin(userId: String) {
        preferences.edit().putString(KEY_LAST_LOGIN_USER_ID, userId).apply()
        preferences.edit().putBoolean(KEY_AUTO_LOGIN, true).apply()
    }

    fun onUserLogout() {
        preferences.edit().putBoolean(KEY_AUTO_LOGIN, false).apply()
    }



}