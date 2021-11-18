package com.arms.flowview.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class TestFragment : Fragment() {
    val mTextModel by viewModels<TestViewModel>()



}