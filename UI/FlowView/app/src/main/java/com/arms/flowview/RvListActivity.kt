package com.arms.flowview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arms.flowview.databinding.ActivityRvBinding
import com.arms.flowview.databinding.ItemTvBinding
import com.arms.flowview.rv.*

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class RvListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRvBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRvBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = arrayListOf<StarBean>()
        for (i in 0..20){
            for (z in 0..2){
                if(i%2 == 0){
                    list.add(StarBean("test${z}","MM"))
                }else{
                    list.add(StarBean("newTest${z}","BB"))
                }
            }
        }
        val adapter = StarAdapter(list)
        binding.rv.apply {
            this.adapter = adapter
            this.addItemDecoration(StarDecoration())
        }
    }

}