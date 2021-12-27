package com.arms.flowview.showFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 *    author : heyueyang
 *    time   : 2021/12/27
 *    desc   : 如果只是需要布局展示的情况下使用这个Fragment就够了，传入LayoutID
 *    version: 1.0
 */
class ShowFragment : Fragment() {

    companion object{
        const val LAYOUT_ID = "layout_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(LAYOUT_ID)
        return layoutId?.let { inflater.inflate(it,container,false) }
    }


}