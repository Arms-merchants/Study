package com.arms.flowview.hilt

import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    ActivityRetainedScoped是viewmodel的作用域
 *
 *    version: 1.0
 */
@ActivityScoped
class MyViewModel @Inject constructor(val repository: Repository):ViewModel() {
}