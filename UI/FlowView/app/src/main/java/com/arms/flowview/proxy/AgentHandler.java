package com.arms.flowview.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * author : heyueyang
 * time   : 2022/03/02
 * desc   :
 * version: 1.0
 */
class AgentHandler implements InvocationHandler {

    private Object target;

    public AgentHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(target, args);
        return result;
    }
}
