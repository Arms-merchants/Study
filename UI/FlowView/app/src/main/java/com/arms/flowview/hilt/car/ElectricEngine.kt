package com.arms.flowview.hilt.car

import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/31
 *    desc   :
 *    version: 1.0
 */
class ElectricEngine @Inject constructor() : Engine {
    override fun getEngineType(): String {
        return "电气引起"
    }
}