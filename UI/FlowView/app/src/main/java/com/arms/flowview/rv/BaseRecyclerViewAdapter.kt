package com.arms.flowview.rv

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import java.lang.reflect.*

/**
 * Created by heyueyang on 2021/11/16
 */
abstract class BaseRecyclerViewAdapter<T, V : ViewBinding, VB : BaseViewBindingHolder<V>> constructor(
    private val data: MutableList<T>? = null
) :
    RecyclerView.Adapter<VB>() {

    val TAG = "========="

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VB {
        Log.e(TAG,"onCreateViewHolder")
        return createBaseDataBindingHolder(setViewBindingLayoutInflater(parent.context, parent))
    }

    private fun setViewBindingLayoutInflater(context: Context, parent: ViewGroup): V {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getKCLass(temp, ViewBinding::class.java)
            temp = temp.superclass
        }
        //inflate的方法需要调用这个不然xml当中的属性不会生效，没有给设置layoutmanager
        val method: Method? = z?.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val obj = method?.invoke(null, LayoutInflater.from(context), parent, false)
        return obj as V
    }

    protected open fun createBaseDataBindingHolder(v: V): VB {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            //这里就是一层层的去找BaseViewBindingHolder，子类找不到就去父类找，如果都找不到那就是null了
            z = getKCLass(temp, BaseViewBindingHolder::class.java)
            temp = temp.superclass
        }
        // 泛型擦除会导致z为null
        val vh: VB? = if (z == null) {
            BaseViewBindingHolder(v) as VB
        } else {
            createBaseGenericKInstance(z, v)
        }
        return vh ?: BaseViewBindingHolder(v) as VB
    }

    /**
     * try to create Generic VH instance
     * 这里应该就是构建一个ViewHolder的实例对象
     * @param z
     * @param view
     * @return
     */
    @Suppress("UNCHECKED_CAST")
    private fun createBaseGenericKInstance(z: Class<*>, v: V): VB? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            //分为两种情况处理，如果是内部的非静态类或者其他
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = if (z == BaseViewBindingHolder::class.java) {
                    z.getDeclaredConstructor(ViewBinding::class.java)
                } else {
                    z.getDeclaredConstructor(v.javaClass)
                }
                constructor.isAccessible = true
                constructor.newInstance(this, v) as VB
            } else {
                //这里如果直接取v的class，类型是匹配不上，如果取super的话因为范型擦除的问题只能渠道Object。。。暂时直接写为ViewBinding
                //这里的getDeclaredConstructor必须是具体的数据类型。。所以在自定义viewHolder的时候要做兼容处理
                constructor = if (z == BaseViewBindingHolder::class.java) {
                    z.getDeclaredConstructor(ViewBinding::class.java)
                } else {
                    z.getDeclaredConstructor(v.javaClass)
                }
                constructor.isAccessible = true
                constructor.newInstance(v) as VB
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

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

    override fun onBindViewHolder(holder: VB, position: Int) {
        Log.e(TAG,"onBindViewHolder${position}")
        convert(holder, data?.get(position))
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract fun convert(holder: VB, item: T?)

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}