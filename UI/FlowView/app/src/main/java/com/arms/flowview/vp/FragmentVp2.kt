package com.arms.flowview.vp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.arms.flowview.ListFragment
import com.arms.flowview.R
import com.arms.flowview.adapter.FAdapter
import com.arms.flowview.databinding.FragmentVp2Binding
import com.arms.flowview.ext.logE
import com.arms.flowview.utils.BarUtils
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
        binding = FragmentVp2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        activity?.window?.let { BarUtils.setStatusBarLightMode(it, true) }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolBar)
            this.actionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        binding.toolBar.navigationIcon?.setTint(Color.GREEN)
        binding.toolBar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
        //如果这里没有改颜色那么这里获取到的就是绿色的图标，并使默认的白色的，但是这里修改为蓝色，并不会影响到之前的设置的绿色图标
        val backDraw = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
        backDraw?.setTint(Color.BLUE)
        binding.iv.setImageDrawable(backDraw)
        val list = arrayListOf<Fragment>(
            ListFragment.createInstance(0),
            ListFragment.createInstance(1),
            ListFragment.createInstance(2),
            ListFragment.createInstance(3),
            ListFragment.createInstance(4),
            ListFragment.createInstance(5)
        )
        val adapter = FAdapter(list, activity as FragmentActivity)
        binding.vp2.adapter = adapter

        val content = arrayListOf<String>("item1", "item2", "item3","item4", "item5", "item6")

        TabLayoutMediator(
            binding.tabLayout, binding.vp2
        ) { tab, position ->
            tab.text = content[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
    }


}