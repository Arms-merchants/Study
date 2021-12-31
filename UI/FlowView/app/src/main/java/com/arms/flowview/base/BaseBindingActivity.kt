package com.arms.flowview.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.GenericSignatureFormatError
import java.lang.reflect.MalformedParameterizedTypeException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 *    author : heyueyang
 *    time   : 2021/11/24
 *    desc   : 电气引擎
 *    version: 1.0
 */
open abstract class BaseBindingActivity<T : ViewBinding> : AppCompatActivity() {

    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var temp: Class<*>? = javaClass
        val bindingClazz = getKCLass(temp!!, ViewBinding::class.java)
        //inflate的方法需要调用这个不然xml当中的属性不会生效，没有给设置layoutmanager
        val method: Method? = bindingClazz?.getMethod(
            "inflate",
            LayoutInflater::class.java
        )
        binding = method?.invoke(null, layoutInflater) as T
        setContentView(binding.root)
        initView()
    }

    abstract fun initView()

    /**
     * 在类的范型上查询符合需求的class
     * 例如在当前的范型上查询当前的ViewBinding的具体的Type是什么，例如ItemTvBinding
     * 还有去查询ViewHolder的类型，这里其实是为了如果有需求可以自定义ViewHolder去查询的，如果没有自定义的话那么拿到的都是BaseViewBindingHolder
     * @param z         需要被查找的class
     * @param findClazz 目标class类型
     */
    private fun getKCLass(z: Class<*>, findClazz: Class<*>): Class<*>? {
        try {
            val type = z.genericSuperclass
            if (type is ParameterizedType) {
                val types = type.actualTypeArguments
                for (temp in types) {
                    if (temp is Class<*>) {
                        if (findClazz.isAssignableFrom(temp)) {
                            return temp
                        }
                    } else if (temp is ParameterizedType) {
                        val rawType = temp.rawType
                        if (rawType is Class<*> && findClazz.isAssignableFrom(
                                rawType
                            )
                        ) {
                            return rawType
                        }
                    }
                }
            }
        } catch (e: GenericSignatureFormatError) {
            e.printStackTrace()
        } catch (e: TypeNotPresentException) {
            e.printStackTrace()
        } catch (e: MalformedParameterizedTypeException) {
            e.printStackTrace()
        }
        return null
    }

}