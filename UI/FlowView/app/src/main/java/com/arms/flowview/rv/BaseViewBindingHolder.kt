package com.arms.flowview.rv

import androidx.viewbinding.ViewBinding

/**
 * Created by heyueyang on 2021/11/16
 */
open  class BaseViewBindingHolder<out VB : ViewBinding>(val vb: VB) : BaseViewHolder(vb.root) {

}