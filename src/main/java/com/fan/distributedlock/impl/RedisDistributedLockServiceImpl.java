package com.fan.distributedlock.impl;

import com.fan.utils.ProcessUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author:fanwenlong
 * @date:2018-05-07 16:28:25
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component("redis")
public class RedisDistributedLockServiceImpl implements DistributedLockService{
    private static Logger logger = Logger.getLogger(RedisDistributedLockServiceImpl.class);
    @Autowired
    RedisConnectionPoolService redisService;

    private final ThreadLocal threadLocal = new ThreadLocal();

    private final String lock = "OnlyLoveCanSaveMe";

    @Override
    public boolean tryLock() {
        System.out.println("we do shit in this method.");
        return false;
    }

    @Override
    public boolean tryRelease() {
        System.out.println("we do shit in this method.");
        return false;
    }

    @Override
    public boolean lock() {
        String value = ProcessUtil.getRandomProcessId();
        threadLocal.set(value);
        int count = 5;
        while (count-- > 0){
            if(tryLock(lock,value) == true){
                return true;
            }
            logger.info("Redis hold lock failed for [" + (5 - count) + "] times");
        }
        return false;
    }

    @Override
    public boolean release() {
        String expectValue = (String) threadLocal.get();
        return tryRelease(lock,expectValue);
    }

    @Override
    public boolean tryRelease(String key, String expectedValue) {
        return tryRelease(key,expectedValue,null);
    }

    public boolean tryRelease(String key, String expectedValue,String templateName){
        RedisTemplate<String,String> template = redisService.getSpecialTemplate((templateName == null || templateName.isEmpty()) ? "template11" : templateName);
        if(template == null){
            return false;
        }
        String actualValue = template.opsForValue().get(key);
        if(expectedValue.equals(actualValue)){
            template.delete(key);
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(String key, String value) {
        return tryLock(key,value,null);
    }

    public boolean tryLock(String key, String value,String templateName){
        RedisTemplate<String,String> template = redisService.getSpecialTemplate((templateName == null || templateName.isEmpty()) ? "template11" : templateName);
        if(template == null){
            return false;
        }
        Boolean res = template.opsForValue().setIfAbsent(key,value);
        if(res == true){
            template.expire(key,100, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    public boolean lockWithMultiServer(){
        int count = redisService.getTemplateCount();
        if(count < 5){
            logger.info("redis cluster num must bigger then 5.");
            return false;
        }

        return false;
    }

    public boolean releaseWithMultiServer(){
        return true;
    }
}
