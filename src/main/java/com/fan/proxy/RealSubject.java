package com.fan.proxy;

/**
 * @author:fanwenlong
 * @date:2018-04-29 14:32:22
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class RealSubject implements Subject{
    @Override
    public void sent() {
        System.out.println("I want to rent my house.");
    }

    @Override
    public void sayHello(String msg) {
        System.out.println("Hello " + msg);
    }
}
