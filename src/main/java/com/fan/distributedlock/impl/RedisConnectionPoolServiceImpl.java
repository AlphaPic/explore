package com.fan.distributedlock.impl;

import com.fan.distributedlock.consts.REDIS_TEMPLATE_NAME;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author:fanwenlong
 * @date:2018-05-09 17:07:00
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component
public class RedisConnectionPoolServiceImpl implements RedisConnectionPoolService{
    @Autowired
    @Qualifier("template11")
    RedisTemplate<String,String> template11;

    @Autowired
    @Qualifier("template12")
    RedisTemplate<String,String> template12;

    @Autowired
    @Qualifier("template13")
    RedisTemplate<String,String> template13;

    @Autowired
    @Qualifier("template21")
    RedisTemplate<String,String> template21;

    @Autowired
    @Qualifier("template22")
    RedisTemplate<String,String> template22;

    @Autowired
    @Qualifier("template23")
    RedisTemplate<String,String> template23;

    @Autowired
    @Qualifier("template31")
    RedisTemplate<String,String> template31;

    @Autowired
    @Qualifier("template32")
    RedisTemplate<String,String> template32;

    @Autowired
    @Qualifier("template33")
    RedisTemplate<String,String> template33;

    private int templateCount;

    @Override
    public RedisTemplate<String, String> getSpecialTemplate(String name) {
        REDIS_TEMPLATE_NAME condition = REDIS_TEMPLATE_NAME.getTemplateByName(name);
        switch (condition){
            case TEMPLATE11:
                return template11;
            case TEMPLATE12:
                return template12;
            case TEMPLATE13:
                return template13;
            case TEMPLATE21:
                return template21;
            case TEMPLATE22:
                return template22;
            case TEMPLATE23:
                return template23;
            case TEMPLATE31:
                return template31;
            case TEMPLATE32:
                return template32;
            case TEMPLATE33:
                return template33;
            case TEMPLATE_ERROR:
            default:
                    break;
        }
        return null;
    }

    @Override
    public RedisTemplate<String, String>[] getAllTemplate() {
        RedisTemplate<String, String>[] templates =  new RedisTemplate[]{ template11,template12,template13,
                                                                template21,template22,template23,
                                                                template31,template32,template33,};
        this.templateCount = templates.length;
        return templates;
    }

    @Override
    public int getTemplateCount() {
        return this.templateCount;
    }


}
