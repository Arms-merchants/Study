package com.arms.flowview.hilt

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    version: 1.0
 */
@HiltViewModel
class FragmentViewModel @Inject constructor(val repository: Repository):ViewModel() {

}