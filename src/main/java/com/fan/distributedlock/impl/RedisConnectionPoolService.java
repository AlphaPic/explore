package com.fan.distributedlock.impl;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author:fanwenlong
 * @date:2018-05-09 17:05:30
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public interface RedisConnectionPoolService {
    RedisTemplate<String,String> getSpecialTemplate(String name);

    RedisTemplate<String,String>[] getAllTemplate();

    int getTemplateCount();
}
