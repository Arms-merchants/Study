package com.arms.flowview.motion

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import com.arms.flowview.R
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentMotionCarBinding
import com.arms.flowview.ext.logE

/**
 *    author : heyueyang
 *    time   : 2021/12/16
 *    desc   :
 *    version: 1.0
 */
class CarMotionFragment : BaseBindingFragment<FragmentMotionCarBinding>() {

    var images = intArrayOf(
        R.drawable.car1,
        R.drawable.car3,
        R.drawable.car4,
        R.drawable.car2
    )

    override fun initView() {
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return 4
            }

            override fun populate(view: View?, index: Int) {
                if (view is ImageView) {
                    val id = view.id
                    val name =   resources.getResourceEntryName(id)
                    "index:${index} ----viewId:${name}".logE()
                    view.setImageResource(images[index])
                }
            }

            override fun onNewItem(index: Int) {

            }

        })
    }
}