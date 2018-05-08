package com.fan.distributedlock.impl;

import org.springframework.stereotype.Component;

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
    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryRelease() {
        return false;
    }

    @Override
    public boolean lock() {
        return false;
    }

    @Override
    public boolean release() {
        return false;
    }
}
