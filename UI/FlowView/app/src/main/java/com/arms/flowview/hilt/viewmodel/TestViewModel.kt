package com.arms.flowview.hilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    version: 1.0
 */
class TestViewModel @AssistedInject constructor(@Assisted val pokeName:String):ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory{
        fun create(name:String):TestViewModel
    }

    companion object{
        fun provideFactory(
            factory: AssistedFactory,
            name:String
        ):ViewModelProvider.Factory = object :ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(name) as T
            }
        }
    }
}