package com.arms.flowview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.R

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   :
 *    version: 1.0
 */
class RvAdapter(val data:ArrayList<String>): RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv.text = data[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tv,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv)
    }

}