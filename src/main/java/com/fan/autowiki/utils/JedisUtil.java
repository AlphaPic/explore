package com.fan.autowiki.utils;

import redis.clients.jedis.Jedis;

/**
 * @author:fanwenlong
 * @date:2018-06-11 14:11:27
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:Redis操作工具类
 * @detail:
 */
public class JedisUtil {

    private static Jedis jedis;

    static {
        jedis = new Jedis("127.0.0.1",6379);
    }

    public static String getStringValue(String key){
        checkStringNull(key);
        return jedis.get(key);
    }

    public static void setStringValue(String key,String value){
        checkStringNull(key);
        jedis.set(key,value);
    }

    public static Long addIntegerValue(String key){
        checkStringNull(key);
        return jedis.incr(key);
    }

    private static void checkStringNull(String value){
        if(value == null || value.isEmpty())
            throw new NullPointerException(value + "为空!!!");
    }
}
