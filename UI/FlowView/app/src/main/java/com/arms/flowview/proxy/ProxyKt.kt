package com.arms.flowview.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 *    author : heyueyang
 *    time   : 2022/03/02
 *    desc   : kotlin的动态代理
 *    version: 1.0
 */
interface IRentHouseKT {
    // 带领租客看房
    fun visitHouse()

    // 讨价还价
    fun argueRent(rent: Int)

    // 签合同
    fun signAgreement()
}

// HouseOwnerKT.kt    2、然后是被代理类，实现接口
class HouseOwnerKT : IRentHouseKT {
    override fun visitHouse() {
        println("HouseOwner 带领看房，介绍房屋特点")
    }

    override fun argueRent(rent: Int) {
        println("HouseOwner 提出租金为：$rent")
    }

    override fun signAgreement() {
        println("HouseOwner 签合同")
    }
}

class HouseOwnerKT2 : IRentHouseKT {
    override fun visitHouse() {

    }

    override fun argueRent(rent: Int) {

    }

    override fun signAgreement() {

    }
}


// AgentHandlerKT.kt    3、建立代理关系
class AgentHandlerKT : InvocationHandler {

    private var mTarget: Any? = null

    constructor(target: Any?) {
        this.mTarget = target
    }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        println("方法执行前")
        // 因为传来的参数 args 是不确定的，所以用 *args.orEmpty() 传参
        val result = method.invoke(mTarget, *args.orEmpty())
        println("方法执行后")
        return result
    }
}

class HouseAgentSmartKT {
    var mHouseOwner: IRentHouseKT? = null

    constructor(houseOwner: IRentHouseKT) {
        mHouseOwner = houseOwner
        mHouseOwner = Proxy.newProxyInstance(
            houseOwner.javaClass.classLoader,
            arrayOf(IRentHouseKT::class.java),
            AgentHandlerKT(mHouseOwner)
        ) as IRentHouseKT
    }
}

class Test {
    fun test() {

        val smartAgent = HouseAgentSmartKT(HouseOwnerKT()).mHouseOwner
        smartAgent?.visitHouse()
        smartAgent?.argueRent(111)

        val smartAgent2 = HouseAgentSmartKT(HouseOwnerKT2()).mHouseOwner
        smartAgent2?.visitHouse()
    }
}



