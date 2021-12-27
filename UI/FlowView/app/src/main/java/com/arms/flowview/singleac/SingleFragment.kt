package com.arms.flowview.singleac

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.arms.flowview.R
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentTop1Binding
import com.arms.flowview.databinding.ItemIndexBoxBinding
import com.arms.flowview.rv.BaseRecyclerViewAdapter
import com.arms.flowview.rv.BaseViewBindingHolder
import com.arms.flowview.showFragment.ShowFragment
import com.arms.flowview.utils.ConverUtils

/**
 *    author : heyueyang
 *    time   : 2021/12/07
 *    desc   : 单一fragment的入口
 *    version: 1.0
 */
class SingleFragment : BaseBindingFragment<FragmentTop1Binding>() {

    override fun initView() {
        val list = arrayListOf<Pair<String, Int>>().apply {
            add("Navigate跳转" to R.id.action_1_to_vp2)
            add("NestedScroll" to R.id.action_11_to_base_show)
            add("Tab" to R.id.action_11_to_base_show)
            add("Drag" to R.id.action_4_to_drag)
            add("MotionCard" to R.id.action_5_to_card_motion)
            add("MotionSwipe" to R.id.action_6_to_motion_swipe)
            add("MotionCar" to R.id.action_7_to_motion_car)
            add("ImageMotion" to R.id.action_8_to_image_motion)
            add("Photo" to R.id.action_11_to_base_show)
            add("Point" to R.id.action_11_to_base_show)
        }

        /**
         * 配合上面的action_11_to_base_show来使用，key相同查找layout，然后传给ShowFragment展示，适合只有单个view需要展示的时候使用
         **/
        val justShowFragmentParams  = arrayListOf<Pair<String,Int>>().apply {
            add("NestedScroll" to R.layout.fragment_nested_scroll_test)
            add("Tab" to R.layout.fragment_tab)
            add("Photo" to R.layout.fragment_photo_view)
            add("Point" to R.layout.fragment_multi_point)
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
                if(item == null){
                    return
                }
                holder.vb.bt.text = item.first
                holder.vb.bt.setOnClickListener {
                    val layoutId = justShowFragmentParams.firstOrNull{it.first == item.first}
                    if(layoutId == null){
                        navigate(item.second)
                    }else{
                        val bundle = Bundle()
                        bundle.putInt(ShowFragment.LAYOUT_ID,layoutId.second)
                        navigate(item.second,bundle)
                    }
                }
            }
        }
    }

    private fun navigate(id: Int,bundle: Bundle? = null) {
        NavHostFragment.findNavController(this).navigate(id,bundle)
    }


}