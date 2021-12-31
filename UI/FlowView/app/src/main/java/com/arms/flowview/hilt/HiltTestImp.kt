package com.arms.flowview.hilt

import android.content.Context
import android.widget.Toast
import com.arms.flowview.autoservice.ITest
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 *    author : heyueyang
 *    time   : 2021/12/30
 *    desc   :
 *    version: 1.0
 */
class HiltTestImp @Inject constructor(@ActivityContext val context:Context):ITest {

    var tempId:Int = 0

    override fun setId(id: Int) {
        tempId = id
         Toast.makeText(context,"id:${id}",Toast.LENGTH_LONG).show()
    }

    override fun getIdValue(): Int {
        return tempId
    }
}