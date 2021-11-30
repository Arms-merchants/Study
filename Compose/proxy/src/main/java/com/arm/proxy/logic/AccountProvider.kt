package com.arm.proxy.logic

import android.content.Context
import android.util.Log
import com.arm.base.model.ActionResult
import com.arm.base.model.PersonProfile
import com.arm.base.model.ServerState
import com.arm.base.provider.IAccountProvider
import com.arm.proxy.consts.AppConst
import com.arm.proxy.utils.GenerateUserSig
import com.tencent.imsdk.v2.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 *    author : heyueyang
 *    time   : 2021/11/29
 *    desc   : IM的初始化，登录等操作
 *    version: 1.0
 */
class AccountProvider: IAccountProvider,Converters {

    override val personProfile = MutableStateFlow(PersonProfile.Empty)

    /**
     * MutableSharedFlow：
     * 发生订阅时，需要将过去已经更新的n个值，同步给新的订阅者。
     * 配置缓存策略。
     *
     * replay :当新的订阅者Collect时，发送几个已经发送过的数据给它
     * extraBufferCapacity：减去replay，MutableShareFlow还缓存多少数据
     * onBufferOverflow：缓存策略，丢掉最新值，丢掉最老值和挂起
     *
     *      serverConnectState.tryEmit() 和  serverConnectState.emit()
     *      emit 方法：当缓存策略为 BufferOverflow.SUSPEND 时，emit 方法会挂起，直到有新的缓存空间。
     *      tryEmit 方法：tryEmit 会返回一个 Boolean 值，true 代表传递成功，false 代表会产生一个回调，让这次数据发射挂起，直到有新的缓存空间。
     *
     */
    override val serverConnectState = MutableSharedFlow<ServerState>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * 初始化SDK
     */
    override fun init(context: Context) {
        val config = V2TIMSDKConfig()
        config.logLevel = V2TIMSDKConfig.V2TIM_LOG_WARN
        V2TIMManager.getInstance().addIMSDKListener(object : V2TIMSDKListener() {

            override fun onConnecting() {
                log("onConnecting")
                dispatchServerState(ServerState.Connecting)
            }

            override fun onConnectSuccess() {
                log("onConnectSuccess")
                dispatchServerState(ServerState.ConnectSuccess)
            }

            override fun onConnectFailed(code: Int, error: String) {
                log("onConnectFailed")
                dispatchServerState(ServerState.ConnectFailed)
            }

            override fun onUserSigExpired() {
                log("onUserSigExpired")
                dispatchServerState(ServerState.UserSigExpired)
            }

            override fun onKickedOffline() {
                log("onKickedOffline")
                dispatchServerState(ServerState.KickedOffline)
            }

            override fun onSelfInfoUpdated(info: V2TIMUserFullInfo) {
                refreshPersonProfile()
            }
        })
        V2TIMManager.getInstance().initSDK(context, AppConst.APP_ID, config)
    }

    override suspend fun login(userId: String): ActionResult {
        val formatUserId = userId.lowercase()
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getInstance()
                .login(formatUserId, GenerateUserSig.genUserSig(formatUserId),
                    object : V2TIMCallback {
                        override fun onSuccess() {
                            continuation.resume(ActionResult.Success)
                        }

                        override fun onError(code: Int, desc: String?) {
                            continuation.resume(
                                ActionResult.Failed(
                                    code = code,
                                    reason = desc ?: ""
                                )
                            )
                        }
                    })
        }
    }

    override suspend fun logout(): ActionResult {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getInstance().logout(object : V2TIMCallback {
                override fun onSuccess() {
                    dispatchServerState(ServerState.Logout)
                    continuation.resume(ActionResult.Success)
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(ActionResult.Failed(code = code, reason = desc ?: ""))
                }
            })
        }
    }

    override fun refreshPersonProfile() {
        coroutineScope.launch {
            getSelfProfileOrigin()?.let {
                convertPersonProfile(it)
            }?.let {
                AppConst.personProfile.value = it
                personProfile.value = it
            }
        }
    }

    private suspend fun getSelfProfileOrigin(): V2TIMUserFullInfo? {
        return suspendCancellableCoroutine { continuation ->
            V2TIMManager.getInstance()
                .getUsersInfo(listOf(V2TIMManager.getInstance().loginUser), object :
                    V2TIMValueCallback<List<V2TIMUserFullInfo>> {
                    override fun onSuccess(t: List<V2TIMUserFullInfo>) {
                        continuation.resume(t[0])
                    }

                    override fun onError(code: Int, desc: String?) {
                        continuation.resume(null)
                    }
                })
        }
    }

    override suspend fun updatePersonProfile(
        faceUrl: String,
        nickname: String,
        signature: String
    ): Boolean {
        val originProfile = getSelfProfileOrigin() ?: return false
        return suspendCancellableCoroutine { continuation ->
            originProfile.faceUrl = faceUrl
            originProfile.setNickname(nickname)
            originProfile.selfSignature = signature
            V2TIMManager.getInstance().setSelfInfo(originProfile, object : V2TIMCallback {
                override fun onSuccess() {
                    continuation.resume(true)
                }

                override fun onError(code: Int, desc: String?) {
                    continuation.resume(false)
                }
            })
        }
    }


    private fun dispatchServerState(serverState: ServerState) {
        coroutineScope.launch {
            serverConnectState.emit(serverState)
        }
    }

    private fun log(log: String) {
        Log.e("AccountProvider", log)
    }


}