package com.fan.proxy.cglib;


import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.misc.ProxyGenerator;

import java.lang.reflect.Method;

/**
 * @author:fanwenlong
 * @date:2018-06-29 17:56:21
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class CglibProxy implements MethodInterceptor {
    private Object target;

    private Enhancer enhancer = new Enhancer();

    public CglibProxy() {
    }

    public CglibProxy(Object target) {
        this.target = target;
    }

    public static <T> T proxyTarget(T t){
        Enhancer en = new Enhancer();
        en.setSuperclass(t.getClass());
        en.setCallback(new CglibProxy(t));
        T tt = (T) en.create();
        return tt;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("before handle.");
        Object obj = method.invoke(target,args);
        System.out.println("after handle.");
        return obj;
    }

    protected Object getProxy(Class clazz){
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    /**
     * 把class文件下载到本地
     */
    public static void downloadClassToLocalPath(){
        CglibProxy cp = new CglibProxy();
        SimpleServiceForCglib service = new SimpleServiceForCglib();
        SimpleServiceForCglib serviceProxy = (SimpleServiceForCglib) cp.getProxy(service.getClass());
    }

    public static void main(String[] args){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"target/classes/com/fan/proxy/cglib");
        SimpleServiceForCglib service = CglibProxy.proxyTarget(new SimpleServiceForCglib());
        service.greeting();

        CglibProxy.downloadClassToLocalPath();
    }
}
