package com.arms.flowview.motion

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.helper.widget.Carousel
import com.arms.flowview.R
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentCardMotionBinding

/**
 *    author : heyueyang
 *    time   : 2021/12/15
 *    desc   :
 *    针对布局中的属性解释：Carousel 动画，至少需要三个状态 previous start next
 *    start组件的最初状态
 *    start->previous 代表向前滑动的过程
 *    start->next代表向后滑动的过程
 *    在完成一次动画后，整个界面会重置为stat状态，但控件的映射的数据发生了变化
 *
 *    在MotionScene用于描述MotionLayout
 *    Transition是MotionScene的一部分，用于指定动画的时长，触发因素以及如何移动视图
 *    ConstraintSet，用于指定过度的起始和结束的约束条件
 *
 *    OnSwipe监听的是MotionLayout,而不是指定的id的视图
 *    OnSwipe :
 *    touchAnchorId是所跟踪的视图，它通过移动来响应轻触操作，MotionLayout会将该视图保持与快速滑动的手机相同的距离
 *    touchAnchorSide:用于确定应该跟踪视图的哪一侧。对于要调整大小，遵循复杂路径或一侧的移动数据比另一侧快的视图很重要。
 *    dragDirection用于确定可对该动画效果产生影响的方向（上下左右）
 *
 *    KeyFrameSet是transition的子集，它包含有KeyPosition
 *    KeyPosition来进行动画的路径修改，
 *    KeyPosition:
 *    framePosition:是一个介于0到100的数字，它定义了动画中应用该KeyPosition的时间，其中1代表动画播放到1%的位置，99代表动画播放到99%的位置，
 *    motionTarget:是被该KeyPosition修改路径的视图
 *    keyPositionType:是该KeyPosition修改路径的方式。它可以是parentRelative,pathRelative或deltaRelative
 *    percentX|percentY是指在framePosition按多大百分比来修改路径(值介于0.0到1.0之间)允许使用负数和大于1的值
 *
 *    默认情况下，MotionLayout 会将因修改路径而产生的所有角设为圆角。如果您查看刚刚创建的动画，就会发现月亮会沿着弯曲的曲线路径移动。
 *    对于大多数动画来说，这正是您需要的效果；否则，您可以指定 curveFit 属性来对其进行自定义。
 *
 *
 *    version: 1.0
 */
class CardMotionFragment : BaseBindingFragment<FragmentCardMotionBinding>() {

    val images = intArrayOf(
        R.drawable.card1,
        R.drawable.card2,
        R.drawable.card3,
        R.drawable.card4,
        R.drawable.card5,
    )

    override fun initView() {
        binding.carousel.setAdapter(object : Carousel.Adapter {
            override fun count(): Int {
                return 5
            }

            override fun populate(view: View?, index: Int) {
                if (view is ImageView) {
                    view.setImageResource(images[index])
                }
            }

            override fun onNewItem(index: Int) {

            }

        })
    }
}