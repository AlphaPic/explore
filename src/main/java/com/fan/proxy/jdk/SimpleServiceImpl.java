package com.fan.proxy.jdk;

/**
 * @author:fanwenlong
 * @date:2018-06-29 16:19:20
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:被代理接口的实现
 * @detail:
 */
public class SimpleServiceImpl implements ISimpleService{
    @Override
    public void greeting() {
        System.out.println("Hey,Lily");
    }
}
