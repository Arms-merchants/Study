package com.arms.flowview.motion

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import coil.load
import com.arms.flowview.R
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentMotionImageBinding
import com.arms.flowview.ext.logE

/**
 *    author : heyueyang
 *    time   : 2021/12/17
 *    desc   :
 *    version: 1.0
 */
class ImageMotionFragment : BaseBindingFragment<FragmentMotionImageBinding>() {

    val imags = intArrayOf(
        R.drawable.dm1,
        R.drawable.dm2,
        R.drawable.dm3,
        R.drawable.dm4,
        R.drawable.dm5
    )

    override fun initView() {
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return 5
            }

            override fun populate(view: View?, index: Int) {
                "Cureent index:${index}----carouselIndex:${binding.carousel.currentIndex}".logE()
                if (view is ImageView) {
                    view.load(imags[index])
                }
            }

            override fun onNewItem(index: Int) {
                binding.ivBack.load(imags[index])
            }

        })
    }
}