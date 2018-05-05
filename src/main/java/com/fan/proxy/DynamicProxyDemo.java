package com.fan.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author:fanwenlong
 * @date:2018-04-29 14:33:44
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class DynamicProxyDemo implements InvocationHandler{
    /**
     * Object that was implemented
     */
    private Object subject;

    public DynamicProxyDemo(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Before rent house.");
        System.out.println("method:" + method);

        method.invoke(subject,args);

        System.out.println("After rent house.");
        return null;
    }
}
