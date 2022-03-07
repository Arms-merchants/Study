package com.arms.flowview.list

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
class ExpandAdapter() : BaseNodeAdapter() {

    init {
        addNodeProvider(HeaderNodeProvider())
        addNodeProvider(BodyNodePrvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return if (data[position] is Head) {
            0
        } else {
            1
        }
    }
}