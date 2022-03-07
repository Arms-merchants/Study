package com.arms.flowview.ktx

/**
 *    author : heyueyang
 *    time   : 2022/03/02
 *    desc   :
 *    version: 1.0
 */
class KotlinTest {

    fun test() {


    }

    fun testLanguage(language: Language) {
        when (language) {
            English -> {

            }
            Japanese -> {

            }
            is Chinese -> {

            }
        }
    }

    fun testFor() {
        for (i in 1..10) {
            "1-10"
        }
        for (i in 1 until 10) {
            "1 - 9"
        }
        for (i in 10 downTo 1) {
            "10-1"
        }

        for (i in 1..10 step 2) {
            "1 3 5 7 9"
        }
        repeat(10) {
            //重复执行几次，it是当前的次数
        }

        /**
         * 不可变
         */
        val list = arrayListOf<String>("1", "2", "3")

        val mutableMap = mutableMapOf<String, String>("name" to "jack", "age" to "23")
        mutableMap.remove("name")
        mutableMap.put("1", "1")


    }

    fun testCollectionOperator(){
         val a = arrayOf("4", "0", "7", "i", "f", "w", "0", "9")
         val index = arrayOf(5, 3, 9, 4, 8, 3, 1, 9, 2, 1, 7)

        index.filter {
            it<a.size
        }.map {
            a[it]
        }.reduce { acc, s ->
            "$acc$s"
        }.also {
            print("it")
        }
        //groupBy分组操作符号
       val value =  index.groupBy {
            it %2 ==0
        }
        //value 是一个map，包含两个，true 符合条件的list，false 不符合条件的list

        //反转
        index.reversed()
        index.reverse()
    }



}

/**
 * 密封类 ,类似与枚举，不过可以扩展相应的子类，设置一些参数
 */
sealed class Language
object English : Language()
object Japanese : Language()
class Chinese(var isDialect: Boolean) : Language()
