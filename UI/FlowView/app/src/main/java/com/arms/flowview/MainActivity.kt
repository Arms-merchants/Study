package com.arms.flowview

import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.autoservice.ITest
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityMainBinding
import com.arms.flowview.ext.logE
import com.arms.flowview.hilt.TestData
import com.arms.flowview.utils.KeyboardUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@AndroidEntryPoint
@Route(path = RouteUrl.FlowViewUrl)
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    @Inject lateinit var executor: ExecutorService
    @Inject lateinit var testData: TestData

    override fun initView() {

        "current testData name:${testData.name}".logE()
        executor.execute {
            "我被通过hilt初始化了".logE()
        }

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

        /**
         * 通过ServiceLoader来获取一个接口的所有实例对象，可能会有多个或者没有
         */
        val list = ServiceLoader.load(ITest::class.java)
        if (list != null) {
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                val testImp = iterator.next()
                testImp.setId(123123)
                return
            }
        } else {
            "list is null".logE()
        }


    }
}