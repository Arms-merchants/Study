package com.arms.flowview.ktx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import com.arms.flowview.ext.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.flow.collect

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class TestFragment : Fragment() {
    val mModel by viewModels<TestViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //distinctUntilChanged相当于数据有变化的时候才会收到通知
        //framgnet中要使用viewLifecycleOwner
        mModel.testData.distinctUntilChanged().observe(viewLifecycleOwner, Observer { })

        launchAndRepeatWithViewLifecycle (Lifecycle.State.RESUMED){
            mModel.navigationEvnet.collect {

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        println("onResume")
        KtxTest.CollectionKtx.test4()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}