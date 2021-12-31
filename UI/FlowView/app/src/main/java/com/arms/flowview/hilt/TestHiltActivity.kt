package com.arms.flowview.hilt

import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.arms.flowview.R
import com.arms.flowview.autoservice.ITest
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.configs.RouteUrl
import com.arms.flowview.databinding.ActivityTestHiltBinding
import com.arms.flowview.ext.logE
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    Hilt的使用场景：
 *    1。全局使用的对象，例如线程池对象，网络请求对象
 *    2。在一定范围内共享的对象，例如一些状态数据在当前的Activity中共享（包括这个Activity中的view和Fragment），例如通过Activity进行Fragemnt中的通信，那么这个数据
 *    一般是放在Activity中，那这里就可以设置这个数据的作用域为Activity，那么在Activity的作用域下获取到都是同一个对象。
 *
 *    一个组件可能会被被共享，或者不会被共享但可能会在多处使用，你都可以使用 Hilt 来把它配置成依赖注入的加载方式。
 *
 *    version: 1.0
 */
@AndroidEntryPoint
@Route(path = RouteUrl.HILTESTURL)
class TestHiltActivity : BaseBindingActivity<ActivityTestHiltBinding>() {

    val mViewModel: MyNewViewModel by viewModels()

    //通过依赖注入替代autoService的实现
    @Inject
    lateinit var mITest: ITest;

    override fun initView() {
        binding.tvContent.setOnClickListener {
            mViewModel.repository.data.postValue(77)

        }
       mViewModel.repository.data.observe(this, {
            binding.tvContent.text = "Activity ${it}"
        })
        supportFragmentManager.commit {
            replace(R.id.fragment_content, HiltTestFragment())
        }

        "Activity viewModel:${mViewModel}".logE()
    }
}