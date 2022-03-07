package com.arms.flowview.utils

import androidx.fragment.app.FragmentActivity

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
class TestUtils {



    var list: List<String>? = null
}

fun FragmentActivity.kotlinTest(
    block: (TestUtils.() -> Unit)? = null
): TestUtils {
    return TestUtils().apply {
        list = arrayListOf("aabb")
        block?.invoke(this)
    }
}
