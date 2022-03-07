package com.arms.flowview.list

import android.view.View
import com.arms.flowview.R
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *    author : heyueyang
 *    time   : 2022/03/01
 *    desc   :
 *    version: 1.0
 */
class HeaderNodeProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 0
    override val layoutId: Int
        get() = R.layout.item_index

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val headNode = item as Head
        helper.setText(R.id.tv, headNode.title)
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        getAdapter()?.expandOrCollapse(position);
    }
}