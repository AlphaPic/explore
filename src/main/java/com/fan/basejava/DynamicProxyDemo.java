package com.fan.basejava;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author:fanwenlong
 * @date:2018-04-02 17:34:29
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class DynamicProxyDemo {

    public interface Interface {
        void getMyName();

        String getNameById(String id);
    }

    static class RealObject implements Interface {
        @Override
        public void getMyName() {
            System.out.println("my name is huhx");
        }

        @Override
        public String getNameById(String id) {
            System.out.println("argument id: " + id);
            return "huhx";
        }
    }

    static class DynamicProxy implements InvocationHandler{
        Object proxied;

        public DynamicProxy(Object proxied){
            System.out.println("dynamic constructor class: " + proxied.getClass());
            this.proxied = proxied;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("dynamic proxy name:" + proxy.getClass());
            System.out.println("method:" + method.getName());
            System.out.println("args:" + Arrays.toString(args));

            Object invokeObject = method.invoke(proxy,args);
            if(invokeObject != null){
                System.out.println("invoke object:" + invokeObject.getClass());
            }else {
                System.out.println("invoke object is null.");
            }
            return invokeObject;
        }
    }

    public static void consumer(Interface iface){
        iface.getMyName();
        String name = iface.getNameById("1");
        System.out.println("name: " + name);
    }

    public static void main(String[] args){
        RealObject realObject = new RealObject();
        consumer(realObject);
        System.out.println("==============================");

        // 动态代理
        ClassLoader classLoader = Interface.class.getClassLoader();
        Class<?>[] interfaces = new Class[] { Interface.class };
        InvocationHandler handler = new DynamicProxy(realObject);
        Interface proxy = (Interface) Proxy.newProxyInstance(classLoader, interfaces, handler);

        System.out.println("in dynamic proxy Main proxy: " + proxy.getClass());
        consumer(proxy);
    }
}
