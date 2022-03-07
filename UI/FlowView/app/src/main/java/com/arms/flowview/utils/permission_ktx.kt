package com.xcf.lazycook.common.ktx

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.arms.flowview.utils.Utils
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * 利用了ActivityResultLauncher的简单权限处理工具
 * @author petterp
 */
class PermissionDialogHelper(
    val title: String,
    val positiveButton: String,
    val message: String
)

class PermissionResultController {
    internal var caller: ActivityResultLauncher<Array<String>>? = null
    private var dialogHelper: PermissionDialogHelper? = null
    var permissions: Array<String> = emptyArray()
    var fail: (() -> Unit)? = null
    var defaultFailMessage: String = ""
    var success: (() -> Unit)? = null

    fun setDialogHelper(
        positiveButton: String = "我知道了",
        title: String,
        message: String
    ) {
        dialogHelper = PermissionDialogHelper(title, positiveButton, message)
    }

    private fun interceptPermission(activity: Activity) {
      /*  if (!isSDK_M) {
            success?.invoke()
            return
        }*/
        val isAll = permissions.any {
            ContextCompat.checkSelfPermission(
                Utils.getApp(),it
            )!= PackageManager.PERMISSION_GRANTED
        }
        if (isAll) {
            dialogHelper?.let {
                AlertDialog.Builder(activity)
                    .setPositiveButton(it.positiveButton) { d, _ ->
                        caller?.launch(permissions)
                        d.dismiss()
                    }
                    .setNegativeButton("拒绝") { d, _ ->
                        d.dismiss()
                    }
                    .setCancelable(false)
                    .setTitle(it.title)
                    .setMessage(it.message)
                    .show()
                return
            } ?: fail?.invoke()
        }
        success?.invoke()
    }

    /** 判断是否永久拒绝,
     * Ps: shouldShowRequestPermissionRationale方法只有用户选择过一次权限后，后续才会判断正确 */
    private fun isPermanentlyRefuse(activity: Activity): Boolean {
        val isRefuse = permissions.any {
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
        if (isRefuse) {
            AlertDialog.Builder(activity)
                .setNegativeButton("取消") { it, _ ->
                    it.dismiss()
                }
                .setPositiveButton("去设置") { d, _ ->
                    //SettingIntentUtil.gotoApplicationPermission(activity)
                    d.dismiss()
                }
                .setCancelable(false)
                .setMessage("您已永久拒绝此权限")
                .show()
        }
        return isRefuse
    }

    fun launch(activity: Activity, obj: (PermissionResultController.() -> Unit)? = null) {
        obj?.invoke(this)
        interceptPermission(activity)
    }

    suspend fun toResult(activity: Activity) =
        suspendCancellableCoroutine<Boolean> {
            launch(activity) {
                success = {
                    it.resume(true)
                }
                fail = {
                    it.resume(false)
                }
            }
        }
}

fun FragmentActivity.createPermissionResultCaller(
    block: (PermissionResultController.() -> Unit)? = null
) =
    PermissionResultController().apply {
        block?.invoke(this)
        caller =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
                val isAll = it.all {
                    it.value
                }
                if (isAll) success?.invoke()
                else fail?.invoke() ?: Toast.makeText(
                   Utils.getApp(),
                    defaultFailMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

fun Fragment.createPermissionResultCaller(block: (PermissionResultController.() -> Unit)? = null) =
    PermissionResultController().apply {
        block?.invoke(this)
        caller =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
                val isAll = it.all {
                    it.value
                }
                if (isAll) success?.invoke()
                else fail?.invoke() ?: Toast.makeText(
                    Utils.getApp(),
                    defaultFailMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
