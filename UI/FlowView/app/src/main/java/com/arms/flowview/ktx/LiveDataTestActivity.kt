package com.arms.flowview.ktx

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arms.flowview.base.BaseBindingActivity
import com.arms.flowview.databinding.ActivityLivedateTestBinding
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 *    author : heyueyang
 *    time   : 2021/12/02
 *    desc   :
 *    version: 1.0
 */

/**
 *
 * 随手的笔记待整理：
 * LiveData会保证订阅者总能在值变化的时候观察到最新的值，并且每个初次订阅的观察者都会执行一次回调方法。
 * 这样的特性对于维持 UI 和数据的一致性没有任何问题，但想要观察LiveData来发射一次性的事件就超出了其能力范围。
 *
 *
 */

class LiveDataTestActivity : BaseBindingActivity<ActivityLivedateTestBinding>() {

    private val mModel by viewModels<TestViewModel>()

    override fun initView() {
        testLifecycleScope()

        mModel.result.observe(this, {
            binding.tv1.text = it
        })
        mModel.testData.observe(this, {
            binding.tv1.text = it
        })
        lifecycleScope.launch {
            mModel.sumFlow.collect {
                binding.tv2.text = it.toString()
            }
        }
        binding.btAdd.setOnClickListener {
            mModel.aModel.inc()
            mModel.bModel.inc()
        }
        testLogin()
    }


    private fun testLogin() {
        val handleState: (NavigationState) -> Unit = {
            when (it) {
                NavigationState.EmptyNavigationState -> {

                }
                NavigationState.ViewNavigationState -> {
                    Snackbar.make(binding.root, "登录蹭个", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        //....这个是为什么？？必要要在同一个的CoroutineScope(这个是错的。。),但是还要分别加launch！！！。。。
        /**
         * --------Flow 的 collect 方法不能写在同一个 lifecycleScope 中--------
         */
        lifecycleScope.launch {
            //写一块只会执行头一个，因为第一个的collect在协程取消前挂起了，后面的执行不到
            //mModel.navigationEvnet.flowWithLifecycle(lifecycle).collect(handleState)
            mModel.navigationState.collect(handleState)
        }

        lifecycleScope.launch {
            mModel.navigationEvnet.flowWithLifecycle(lifecycle).collect(handleState)
            //这个日志是打印不出来的
            Logger.e("我应该是执行不到")
        }

        binding.btLogin.setOnClickListener {
            //mModel.login()
            mModel.login2()
        }
    }

    private fun test() {
        val shareFlow = MutableSharedFlow<Int>()
        val shareFlow2 = MutableSharedFlow<Int>()
        lifecycleScope.launch {
            /**
             *这种写法错误，shareFlow.collect在协程被取消前会一直挂起，后面的代码不会执行
             */
            shareFlow.collect {
                Logger.e("shareFlow ${it}")
            }
            shareFlow2.collect {

            }
        }
    }


    private fun testLifecycleScope() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                Logger.e("testLifecycleScope 当前的线程 delay前：${Thread.currentThread().name}")
                mModel.doRequest()
                Logger.e("testLifecycleScope 当前的线程delay后：${Thread.currentThread().name}")
                Toast.makeText(this@LiveDataTestActivity, "创建执行", Toast.LENGTH_LONG).show()
            }
        }
        Logger.e("testLifecycleScope")

    }



}