package com.arms.flowview.proxy;

import java.lang.reflect.Proxy;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
class HouseAgentSmart {
    private IRentHouse mHouseOwner;

    public HouseAgentSmart(IRentHouse houseOwner) {
        mHouseOwner = houseOwner;
        mHouseOwner = (IRentHouse) Proxy.newProxyInstance(
                houseOwner.getClass().getClassLoader(),
                new Class[]{IRentHouse.class},
                new AgentHandler(mHouseOwner)
        );
    }

    public IRentHouse getAccess() {
        return mHouseOwner;
    }
}


