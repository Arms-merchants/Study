package com.arms.flowview

import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityMainBinding
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.KeyboardUtils
import com.orhanobut.logger.Logger

@Route(path = RouteUrl.FlowViewUrl)
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun initView() {
        KeyboardUtils.fixAndroidBug5497(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //这里就是只要你够快在线程检查之前去修改那么就没有问题，这里就是要查看Activity的启动流程
        Thread {
            binding.tv1.text = "子线程修改"
        }.start()
        //监听软键盘的弹出。。。
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.et
        ) { v, insets ->
            val windowInsets = insets ?: ViewCompat.getRootWindowInsets(binding.root)
            val imeVisible = windowInsets!!.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            "imeVisible:${imeVisible}----imeHeight:${imeHeight}".logE()

            windowInsets
        }
        binding.btCloseIme.setOnClickListener {
            binding.ivTest.offsetLeftAndRight(200)
        }
        binding.et.setText("12312313")
        Logger.e("123123123")
        Logger.e("123123123")
        Logger.e("123123123")
        Logger.e("123123123")
    }
}