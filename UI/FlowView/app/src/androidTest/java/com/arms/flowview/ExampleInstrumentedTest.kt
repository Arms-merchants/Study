package com.arms.flowview

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {

        val mutableMap = mutableMapOf<String, String>("name" to "jack", "age" to "23")
        mutableMap.remove("name")
        mutableMap.put("1", "1")
        /**
         * 不可变
         */
        val list = arrayListOf<String>("1", "2", "3")





    }
}