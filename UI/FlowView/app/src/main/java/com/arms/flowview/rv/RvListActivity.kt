package com.arms.flowview.rv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.arms.flowview.databinding.ActivityRvBinding
import com.arms.flowview.rv.card.*

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
        val list = ArrayList<StarBean>()
        /* for (i in 0..100) {
             for (z in 0..2) {
                 if (i % 2 == 0) {
                     list.add(StarBean("test${z}", "MM"))
                 } else {
                     list.add(StarBean("newTest${z}", "BB"))
                 }
             }
         }*/
        for (i in 0..5) {
            list.add(StarBean("test${i}", "MM"))
        }

        /*      val newAdapter = object :
                  BaseRecyclerViewAdapter<StarBean, ItemTvBinding, BaseViewBindingHolder<ItemTvBinding>>() {
                  override fun convert(holder: BaseViewBindingHolder<ItemTvBinding>, item: StarBean?) {
                      holder.vb.tv.text = item?.name
                  }
              }
      */
        val adapter = StarAdapter(list)
        CardConfig.initConfig()
        binding.rv.apply {
            //this.layoutManager = GridLayoutManager(this@RvListActivity, 5)
            this.layoutManager = CardLayoutManager()
            this.adapter = adapter
            //this.addItemDecoration(StarDecoration())
        }

        val newCallBack = SlideCallback(binding.rv,adapter,list)

        val callBack = CardCallBack(list as MutableList<Any>, object : CardCallBack.OnSwipCallback {
            override fun onSwiped(data: List<*>?) {
                adapter.notifyDataSetChanged()
            }
        }, adapter)
        val itemTouchHelper = ItemTouchHelper(newCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rv)
    }

}