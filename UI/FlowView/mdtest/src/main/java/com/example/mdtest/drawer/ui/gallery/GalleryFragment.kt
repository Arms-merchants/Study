package com.example.mdtest.drawer.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mdtest.R

class GalleryFragment : Fragment() {
    private val galleryViewModel: GalleryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_gallery, container, false)
        val textView = root.findViewById<TextView>(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, { s -> textView.text = s })
        return root
    }
}