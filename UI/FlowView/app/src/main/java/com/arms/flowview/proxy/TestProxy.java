package com.arms.flowview.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
class TestProxy {

    private void test() {
        //为Map创建一个代理对象
        Map mapProxy = (Map) Proxy.newProxyInstance(HashMap.class.getClassLoader(),
                new Class[]{Map.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                }
        );
    }

    private void testProxy() {
        IRentHouse smartAgent = new HouseAgentSmart(new HouseOwner()).getAccess();
        smartAgent.visitHouse();
        smartAgent.argueRent(400);
        smartAgent.signAgreement();
    }


}
