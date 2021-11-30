package com.arm.proxy.consts

import com.arm.base.model.PersonProfile
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   :
 *    version: 1.0
 */
internal object AppConst {

    const val APP_ID = 1400592743

    const val APP_SECRET_KEY = "9b9d7ea10c1d88a377e31b19320ed8780a12f55451a76461b3a87189ee7339e0"

    val personProfile = MutableStateFlow(PersonProfile.Empty)
}