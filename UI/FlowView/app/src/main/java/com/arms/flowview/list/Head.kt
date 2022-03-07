package com.arms.flowview.list

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
class Head(val childNodes: MutableList< BaseNode>, val title: String) : BaseExpandNode() {

    init {
        isExpanded = false
    }

    override val childNode: MutableList<BaseNode>?
        get() = childNodes

}