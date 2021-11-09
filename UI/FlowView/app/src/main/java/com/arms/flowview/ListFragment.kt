package com.arms.flowview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.adapter.RvAdapter

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   :
 *    version: 1.0
 */
class ListFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list,null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val list = arrayListOf<String>()
        for(i in 0..100){
            list.add(i.toString())
        }
        val adapter = RvAdapter(list)
        rv.adapter = adapter
    }
}