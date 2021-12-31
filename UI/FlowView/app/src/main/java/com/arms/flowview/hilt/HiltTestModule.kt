package com.arms.flowview.hilt

import com.arms.flowview.autoservice.ITest
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   : 返回接口的实例对象
 *    返回接口对象的话需要使用abstract以及@Binds
 *    version: 1.0
 */
@Module
@InstallIn(ActivityComponent::class)
abstract  class HiltTestModule {

    @Binds
   abstract fun providesTestImp(hiltTestImp: HiltTestImp):ITest
}

/*@Module
@InstallIn(ActivityComponent::class)
object DataModule{

    @Singleton
    @Provides
    fun getRepositoryData():Repository{
        return  Repository()
    }
}*/
