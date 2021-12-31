package com.arms.flowview.hilt.viewmodel

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    version: 1.0
 */
@AndroidEntryPoint
class TestActivity:AppCompatActivity() {

    @Inject lateinit var factory:TestViewModel.AssistedFactory

    val viewModel:TestViewModel by viewModels {
        TestViewModel.provideFactory(factory,"123123")
    }
}
