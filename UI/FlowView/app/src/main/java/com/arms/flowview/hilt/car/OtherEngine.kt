package com.arms.flowview.hilt.car

import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/31
 *    desc   :
 *    version: 1.0
 */
class OtherEngine @Inject constructor() : Engine {
    override fun getEngineType(): String {
        return "其他类型引擎"
    }
}