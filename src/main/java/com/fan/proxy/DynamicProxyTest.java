package com.fan.proxy;

import java.lang.reflect.Proxy;

/**
 * @author:fanwenlong
 * @date:2018-04-29 14:37:19
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class DynamicProxyTest {
    public static void main(String[] args){
        Subject subject = (Subject) Proxy.newProxyInstance(RealSubject.class.getClassLoader(),RealSubject.class.getInterfaces(),new DynamicProxyDemo(new RealSubject()));
        subject.sent();
        subject.sayHello("Jack");
    }
}
