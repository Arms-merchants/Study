package com.arms.flowview.rv

import android.widget.TextView
import com.arms.flowview.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * Created by heyueyang on 2021/11/17
 */
class StartAdapterByBase(data:MutableList<String>?) :BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_tv,data) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.getView<TextView>(R.id.tv).text = item
    }
}