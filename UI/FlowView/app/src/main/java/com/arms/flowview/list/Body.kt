package com.arms.flowview.list

import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
class Body(val content:String) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null
}