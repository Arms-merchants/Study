package com.arms.flowview.ktx

/**
 *    author : heyueyang
 *    time   : 2022/03/02
 *    desc   :
 *    version: 1.0
 */
interface IRentHouse {
    // 带领租客看房
    fun visitHouse()

    // 讨价还价
    fun argueRent(rent: Int)

    // 签合同
    fun signAgreement()
}

class HouseOwner : IRentHouse {
    override fun visitHouse() {
        println("带领看房，介绍房屋特点")
    }

    override fun argueRent(rent: Int) {
        println("提出租金为：$rent")
    }

    override fun signAgreement() {
        println("签合同")
    }
}

class HouseAgent(houseOwner: HouseOwner) : IRentHouse by houseOwner {}

class Test {
    fun test() {
        //静态代理，也就是房屋中介有房主的权利
        val houseOwner = HouseOwner()
        HouseAgent(houseOwner).visitHouse()

    }
}

