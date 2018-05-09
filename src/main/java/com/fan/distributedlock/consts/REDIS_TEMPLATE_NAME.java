package com.fan.distributedlock.consts;

/**
 * @author:fanwenlong
 * @date:2018-05-09 17:09:24
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public enum REDIS_TEMPLATE_NAME {
    TEMPLATE11("template11"),
    TEMPLATE12("template12"),
    TEMPLATE13("template13"),
    TEMPLATE21("template21"),
    TEMPLATE22("template22"),
    TEMPLATE23("template23"),
    TEMPLATE31("template31"),
    TEMPLATE32("template32"),
    TEMPLATE33("template33"),
    TEMPLATE_ERROR("no such template");

    private String name;

    REDIS_TEMPLATE_NAME(String name) {
        this.name = name;
    }

    public static REDIS_TEMPLATE_NAME getTemplateByName(String name){
        if(name == null || name.isEmpty()){
            return TEMPLATE_ERROR;
        }
        REDIS_TEMPLATE_NAME[] redisNameArr = REDIS_TEMPLATE_NAME.values();
        for (REDIS_TEMPLATE_NAME redisName : redisNameArr){
            if(redisName.name.equals(name)){
                return redisName;
            }
        }
        return TEMPLATE_ERROR;
    }
}
