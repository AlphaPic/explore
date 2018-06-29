package com.fan.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author:fanwenlong
 * @date:2018-06-29 16:20:55
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:动态代理类
 * @detail:
 */
public class DynamicProxyByJdk implements InvocationHandler{
    private Object ProxyObject;

    public DynamicProxyByJdk(Object proxyObject) {
        ProxyObject = proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before handle.");
        Object returnVal = null;
        try {
            returnVal = method.invoke(ProxyObject, args);
        }catch (Exception e){

        }
        System.out.println("after handle.");
        return returnVal;
    }
}
