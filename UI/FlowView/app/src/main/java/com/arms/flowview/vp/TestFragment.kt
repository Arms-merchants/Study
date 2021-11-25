package com.arms.flowview.vp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arms.flowview.databinding.FragmentVpTestBinding
import com.arms.flowview.ext.logE

/**
 *    author : heyueyang
 *    time   : 2021/11/24
 *    desc   :
 *    version: 1.0
 */
open class TestFragment(val index: Int) : Fragment() {

    lateinit var binding: FragmentVpTestBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        "${index}onAttach".logE()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "${index}onCreate".logE()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVpTestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        "${index}onActivityCreated".logE()
    }

    override fun onStart() {
        super.onStart()
        "${index}onStart".logE()
    }

    override fun onResume() {
        super.onResume()
        "${index}onResume---isVisible${isVisible}".logE()
        binding.faceView.onStart()

    }

    override fun onPause() {
        super.onPause()
        "${index}onPause".logE()
        binding.faceView.stop()
    }

    override fun onStop() {
        super.onStop()
        "${index}onStop".logE()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        "${index}onDestroyView".logE()
    }

    override fun onDestroy() {
        super.onDestroy()
        "${index}onDestroy".logE()
    }

    override fun onDetach() {
        super.onDetach()
        "${index}onDetach".logE()
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        "${index}setMenuVisibility---${menuVisible}".logE()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        "${index}setUserVisibleHint---${isVisibleToUser}".logE()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "${index}onViewCreated".logE()
    }

}