package com.arms.flowview

import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.databinding.ActivityMainBinding
import com.arms.flowview.ext.logE

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    override fun initView() {
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
    }
}