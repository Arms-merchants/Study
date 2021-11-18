package com.arms.flowview.rv

import com.arms.flowview.databinding.ItemTvBinding

/**
 * Created by heyueyang on 2021/11/16
 * 两种方式，分别支持默认的viewHolder和自定义的ViewHolder
 */
class StarAdapter(val data: MutableList<StarBean>?) :
    BaseRecyclerViewAdapter<StarBean, ItemTvBinding, StarAdapter.MyViewHolder>(data) {

    override fun convert(holder: MyViewHolder, item: StarBean?) {
        holder.vb.tv.text = item?.name
    }

    class MyViewHolder(viewBinding: ItemTvBinding):BaseViewBindingHolder<ItemTvBinding>(viewBinding){
        val testStr = "test"
    }

    /**
     * 判断是不是组的开头
     */
    fun isGroupHead(position:Int):Boolean{
        return if(data.isNullOrEmpty()){
            false
        }else{
            if(position ==0){
                true
            }else{
                val groupName = data[position].groupName
                val preGroupName = data[position-1].groupName
                preGroupName != groupName
            }
        }
    }
}

class StartAdapter2(data: MutableList<String>?):
    BaseRecyclerViewAdapter<String,ItemTvBinding,BaseViewBindingHolder<ItemTvBinding>>(data){

    override fun convert(holder: BaseViewBindingHolder<ItemTvBinding>, item: String?) {
       holder.vb.tv.text = item
    }
}
