package com.arms.flowview.proxy;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
// code 2
class HouseOwner implements IRentHouse{
    @Override
    public void visitHouse() {
        System.out.println("HouseOwner 带领看房，介绍房屋特点");
    }

    @Override
    public void argueRent(int rent) {
        System.out.println("HouseOwner 提出租金为：" + rent);
    }

    @Override
    public void signAgreement() {
        System.out.println("HouseOwner 签合同");
    }
}

