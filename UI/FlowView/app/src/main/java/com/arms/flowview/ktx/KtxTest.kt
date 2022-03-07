package com.arms.flowview.ktx

import android.util.Log
import com.orhanobut.logger.Logger

/**
 * <pre>
 *    author : heyueyang
 *    time   : 2021/11/16
 *    desc   :
 *    version: 1.0
 */
class KtxTest private constructor() {

    companion object {
        const val TAG = "========"
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            KtxTest()
        }
    }

    object CollectionKtx {

        fun test1() {
            //两个列表合并
            val combinedList = arrayListOf(1, 2, 3) + arrayListOf(4, 5, 6)
            // 这里的数据添加并不是在原集合上进行的，是一个新的集合
            var newList = arrayListOf(1, 2, 3) + 4 + 5
            newList = newList + 1
            newList.forEach {
                Log.e(TAG, "current${it}")
            }
        }

        fun test2() {
            println("test2")
        }

        fun test() {
            //返回与给定 KClass 实例对应的 Java Class 实例。在原始类型的情况下，它返回相应的包装类。这里指向的是JDK中的Integer.java
            Int::class.javaObjectType
            //这个指向的是Kotlin库中的Int.kt
            Int::class.java

            val book = Book("1", "2")
            book.id = "12"
            //解构
            val (id, bookName) = book
            //这里的bookName拿到的就是book对象中的属性值
            Logger.e("bookName:${bookName}")

            val car = Car("丰田", 12.0)
            val (carName, carPrice) = car
            Logger.e("CarName:${carName}")

            val persons = mapOf<String, String>("name" to "Jack", "age" to "23")
            for ((key, value) in persons) {
                Logger.e("Name${key}-- Age${value}")
            }

        }

        data class Book(var id: String, var name: String)

        /**
         * 普通类进行解构操作
         * operator 关键字可以用来重载操作符或者实现一个约定。这里就是实现了一个约定。这样写之后就可以像 data class 一样进行解构了。
         * Kotlin 的这种解构方式用的比较多的地方是在 Map 数据结构中，它不需要像 Java 一样先拿到 Entry 对象，然后才能拿到 key 值和 value 值，直接用这种解构即可。
         */
        class Car(var brand: String, var price: Double) {
            operator fun component1(): String {
                return this.brand
            }

            operator fun component2(): Double {
                return this.price
            }
        }


        fun test3() {
            println("test2")
        }

        fun test4() {
            println("test2")
        }

    }

}