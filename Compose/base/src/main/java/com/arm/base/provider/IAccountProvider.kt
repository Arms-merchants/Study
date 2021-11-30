package com.arm.base.provider

import android.content.Context
import com.arm.base.model.ActionResult
import com.arm.base.model.PersonProfile
import com.arm.base.model.ServerState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


/**
 * @Author: leavesC
 * @Date: 2021/7/9 22:33
 * @Desc:
 * @Github：https://github.com/leavesC
 */
interface IAccountProvider {

    val personProfile: StateFlow<PersonProfile>

    val serverConnectState: SharedFlow<ServerState>

    fun init(context: Context)

    suspend fun login(userId: String): ActionResult

    suspend fun logout(): ActionResult

    fun refreshPersonProfile()

    suspend fun updatePersonProfile(faceUrl: String, nickname: String, signature: String): Boolean

}