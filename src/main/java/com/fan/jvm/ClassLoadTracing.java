package com.fan.jvm;

/**
 * @author:fanwenlong
 * @date:2018-04-30 14:37:37
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ClassLoadTracing {
    class Demo{

    }
    public static void main(String[] args){
        ClassLoadTracing tracing = new ClassLoadTracing();
        Demo demo = tracing.new Demo();
    }
}
