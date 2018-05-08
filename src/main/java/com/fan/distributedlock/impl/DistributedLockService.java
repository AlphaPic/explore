package com.fan.distributedlock.impl;

/**
 * @author:fanwenlong
 * @date:2018-05-07 13:44:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public interface DistributedLockService {
    /**
     * 非阻塞分布式锁的获取
     * @return
     */
    public boolean tryLock();

    /**
     * 非阻塞分布式锁的释放
     * @return
     */
    public boolean  tryRelease();

    /**
     * 锁住表
     * @return
     */
    public boolean lock();

    /**
     * 释放锁
     * @return
     */
    public boolean release();

}
