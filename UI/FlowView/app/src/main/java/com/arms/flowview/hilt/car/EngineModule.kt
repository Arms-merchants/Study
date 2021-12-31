package com.arms.flowview.hilt.car

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Qualifier

/**
 *    author : heyueyang
 *    time   : 2021/12/31
 *    desc   :
 *    version: 1.0
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ElectricTypeEngine

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OtherTypeEngine


/*
@Module
@InstallIn(ActivityComponent::class)
abstract class EngineModule {

    @ElectricTypeEngine
    @Binds
    abstract fun providerElectricEngine(electricEngine: ElectricEngine): Engine

    @OtherTypeEngine
    @Binds
    abstract fun providerOtherEngine(otherEngine: OtherEngine): Engine
}*/

@Module
@InstallIn(ActivityComponent::class)
object CarModule{

    @ElectricTypeEngine
    @Provides
    fun providesElectricTypeCar(electricEngine: ElectricEngine):Car{
        return Car(electricEngine)
    }

    @OtherTypeEngine
    @Provides
    fun providesOtherTypeCar(otherEngine: OtherEngine):Car{
        return Car(otherEngine)
    }
}

