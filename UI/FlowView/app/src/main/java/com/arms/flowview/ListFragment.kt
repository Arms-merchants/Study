package com.arms.flowview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.adapter.RvAdapter
import com.arms.flowview.ext.logE

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/09
 *    desc   :
 *    version: 1.0
 */
class ListFragment() : Fragment() {

    private var index = 0

    companion object {
        const val INDEX = "index"
        fun createInstance(index: Int): ListFragment {
            val listFragment = ListFragment()
            val bundle = Bundle()
            bundle.putInt(INDEX, index)
            listFragment.arguments = bundle
            return listFragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        index = arguments?.getInt(INDEX, 0) ?: 0
        "ListFragment ${index} onCreateView".logE()
        return inflater.inflate(R.layout.fragment_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "ListFragment ${index} onViewCreated".logE()
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val list = arrayListOf<String>()
        for (i in 0..20) {
            list.add(i.toString())
        }
        val adapter = RvAdapter(list)
        rv.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        "ListFragment ${index} onResume".logE()
    }

    override fun onStart() {
        super.onStart()
        "ListFragment ${index} onStart".logE()
    }

    override fun onStop() {
        super.onStop()
        "ListFragment ${index} onStop".logE()
    }

    override fun onDetach() {
        super.onDetach()
        "ListFragment ${index} onDetach".logE()
    }

    override fun onDestroy() {
        super.onDestroy()
        "ListFragment ${index} onDestroy".logE()

    }


}