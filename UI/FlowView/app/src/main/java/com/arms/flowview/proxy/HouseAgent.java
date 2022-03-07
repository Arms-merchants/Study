package com.arms.flowview.proxy;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
class HouseAgent implements IRentHouse{
    IRentHouse mHouseOwner;    // 中介持有房东的权利

    public HouseAgent(IRentHouse houseOwner) {
        mHouseOwner = houseOwner;
    }

    @Override
    public void visitHouse() {
        mHouseOwner.visitHouse();
    }

    @Override
    public void argueRent(int rent) {
        mHouseOwner.argueRent(rent);
    }

    @Override
    public void signAgreement() {
        mHouseOwner.signAgreement();
    }
}