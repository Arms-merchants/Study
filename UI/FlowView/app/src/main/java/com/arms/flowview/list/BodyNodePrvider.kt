package com.arms.flowview.list

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
class BodyNodePrvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_tv

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val body = item as Body
        helper.setText(R.id.tv, body.content)
    }
}