package com.fan.proxy.jdk;

import sun.misc.ProxyGenerator;
import java.lang.reflect.Proxy;

/**
 * @author:fanwenlong
 * @date:2018-06-29 16:25:24
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class DynamicProxyTestByJdk {
    public static void main(String[] args) {
        //下面这句话设置了是为了生成代理类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        ISimpleService service = (ISimpleService) Proxy.newProxyInstance(SimpleServiceImpl.class.getClassLoader(), SimpleServiceImpl.class.getInterfaces(),new DynamicProxyByJdk(new SimpleServiceImpl()));
        service.greeting();

        ProxyGenerator.generateProxyClass("target/classes/com/fan/proxy/jdk/SimpleServiceImplProxyClass", SimpleServiceImpl.class.getInterfaces());
    }
}
