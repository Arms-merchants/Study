package com.arms.flowview.hilt

import androidx.fragment.app.Fragment
import com.arms.flowview.hilt.car.Car
import com.arms.flowview.hilt.car.OtherTypeEngine
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.FragmentComponent

/**
 *    author : heyueyang
 *    time   : 2021/12/31
 *    desc   :测试在一些非直接支持的地方进行依赖注入
 *    version: 1.0
 */
class TestEntryPoint {

    @EntryPoint
    @InstallIn(FragmentComponent::class)
    interface CarProviderEntryPoint{
        @OtherTypeEngine
        fun providerCar():Car
    }

    fun getCar(fragment:Fragment):Car{
      val carProviderPoint =   EntryPointAccessors.fromFragment(fragment,CarProviderEntryPoint::class.java)
        return carProviderPoint.providerCar()
    }

}