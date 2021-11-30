package com.example.mdtest.drawer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mdtest.R


class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById<TextView>(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, { s -> textView.setText(s) })
        return root
    }
}