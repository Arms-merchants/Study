package com.arms.flowview.vp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.arms.flowview.ListFragment
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.databinding.FragmentVp2Binding
import com.arms.flowview.ext.logE
import com.google.android.material.tabs.TabLayoutMediator

/**
 *    author : heyueyang
 *    time   : 2021/11/25
 *    desc   :
 *    version: 1.0
 */
class FragmentVp2 : Fragment() {

    lateinit var binding: FragmentVp2Binding

    var index = 0

    companion object {
        const val INDEX = "index"
        fun newInstance(index: Int): FragmentVp2 {
            val instance = FragmentVp2()
            val bundle = Bundle()
            bundle.putInt(INDEX, index)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        index = arguments?.getInt(INDEX) ?: 0
        "FragmentVp2 ${index} onCreateView".logE()
        binding = FragmentVp2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "FragmentVp2 ${index} onViewCreated".logE()
        val list = arrayListOf<Fragment>(ListFragment(0), ListFragment(1), ListFragment(2))
        val adapter = FAdapter(list, activity as FragmentActivity)
        binding.vp2.adapter = adapter

        val content = arrayListOf<String>("item1","item2","item3")

        TabLayoutMediator(
            binding.tabLayout, binding.vp2
        ) { tab, position ->
            tab.text = content[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        "FragmentVp2 ${index} onResume".logE()
    }


}