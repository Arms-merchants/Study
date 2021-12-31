package com.arms.flowview.hilt

import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.arms.flowview.base.BaseBindingFragment
import com.arms.flowview.databinding.FragmentTestHiltBinding
import com.arms.flowview.ext.logE
import com.arms.flowview.hilt.car.Car
import com.arms.flowview.hilt.car.ElectricTypeEngine
import com.arms.flowview.hilt.car.OtherTypeEngine
import com.arms.flowview.hilt.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    version: 1.0
 */
@AndroidEntryPoint
class HiltTestFragment : BaseBindingFragment<FragmentTestHiltBinding>() {

    /**
     * 构建带有参数的ViewModel
     */
    @Inject
    lateinit var factory: TestViewModel.AssistedFactory

    @ElectricTypeEngine
    @Inject
    lateinit var elecTypeCar: Car

    @OtherTypeEngine
    @Inject
    lateinit var otherTypeCar: Car


    val viewModel: TestViewModel by viewModels {
        TestViewModel.provideFactory(factory, "123123")
    }

    override fun initView() {
        /* mViewModel.repository.data.observe(viewLifecycleOwner,{
             binding.tvContent.text="Fragment id:${it}"
         })*/
        binding.tvContent.text = viewModel.pokeName

        elecTypeCar.engine.getEngineType().logE()
        otherTypeCar.engine.getEngineType().logE()

        val point = TestEntryPoint()
        point.getCar(this).engine.getEngineType().logE()
    }
}