package com.arms.flowview.singleac

import android.graphics.Rect
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.R
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentTop1Binding
import com.arms.flowview.databinding.ItemIndexBoxBinding
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder
import com.arms.flowview.utils.ConverUtils

/**
 *    author : heyueyang
 *    time   : 2021/12/07
 *    desc   :
 *    version: 1.0
 */
class SingleFragment : BaseBindingFragment<FragmentTop1Binding>() {

    override fun initView() {
        val list = arrayListOf<Pair<String, Int>>().apply {
            add("Navigate跳转" to R.id.action_1_to_vp2)
            add("NestedScroll" to R.id.action_2_nested_scroll)
            add("Tab" to R.id.action_3_to_tab)
            add("Drag" to R.id.action_4_to_drag)
            add("MotionCard" to R.id.action_5_to_card_motion)
            add("MotionSwipe" to R.id.action_6_to_motion_swipe)
            add("MotionCar" to R.id.action_7_to_motion_car)
            add("ImageMotion" to R.id.action_8_to_image_motion)
        }
        binding.rvIndex.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.left = ConverUtils.dp2px(10f)
            }
        })
        binding.rvIndex.adapter = object :
            BaseRecyclerViewAdapter<Pair<String, Int>, ItemIndexBoxBinding, BaseViewBindingHolder<ItemIndexBoxBinding>>(
                list
            ) {
            override fun convert(
                holder: BaseViewBindingHolder<ItemIndexBoxBinding>,
                item: Pair<String, Int>?
            ) {
                holder.vb.bt.text = item?.first
                holder.vb.bt.setOnClickListener {
                    item?.second?.let { it1 -> navigate(it1) }
                }
            }
        }
    }

    private fun navigate(id: Int) {
        NavHostFragment.findNavController(this).navigate(id)
    }


}