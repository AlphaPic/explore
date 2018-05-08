package com.fan.distributedlock.impl;

import com.fan.distributedlock.config.bean.MethodLock;
import com.fan.distributedlock.config.mapper.MethodNameMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author:fanwenlong
 * @date:2018-05-07 13:59:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component("mysql")
public class MysqlDistributedLockServiceImpl implements DistributedLockService {
    private static Logger logger = Logger.getLogger(MysqlDistributedLockServiceImpl.class);
    @Autowired
    private SqlSessionFactory factory;

    /**
     * 使用同步块是为了在同一程序中不同线程可以独享这个factory
     * @return
     */
    @Override
    public boolean tryLock() {
        synchronized (factory) {
            int res = 0;
            SqlSession session = factory.openSession();
            try {
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                res = mapper.insertMethodName("hello","liliyany");
                session.commit();
            } catch (Exception e){
                logger.info(e.getMessage());
            }finally {
                session.close();
                return res == 1 ? true : false;
            }
        }
    }

    @Override
    public boolean tryRelease() {
        return false;
    }

    public boolean tryRelease(String methodName,String expectedValue) {
        synchronized (factory) {
            SqlSession session = factory.openSession();
            Boolean res = false;
            try{
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                MethodLock lock = mapper.getMethodName(methodName);
                if(lock != null && lock.getDesc().equals(expectedValue)) {
                    res = mapper.deleteMethodName(methodName);
                    session.commit();
                }
            }catch (Exception e){
            }finally {
                if(session != null){
                    session.close();
                }
                return res;
            }
        }
    }

    /**
     * 使用表锁来进行同步
     * @return
     */
    public boolean lockWithSelectUpdate(){
        synchronized (factory){
            SqlSession session = factory.openSession();
            try{
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                MethodLock lock = mapper.selectForALock("hello");
            }catch (Exception e){
                logger.info(e.getMessage());
                return false;
            }finally {
                session.commit();
                if(session != null){
                    session.close();
                }
            }
        }
        return true;
    }

    @Override
    public boolean lock(){
        System.out.println("-----------------------------chasing mysql distributed lock start-----------------------------");
        int count = 5;
        while (count-- > 0){
            if(tryLock() == true){
                return true;
            }
            try {
                System.out.println("failed for " + (5 - count) + " times.");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----------------------------chasing mysql distributed lock end-----------------------------");
        return false;
    }

    @Override
    public boolean release(){
        return tryRelease("hello","liliyany");
    }
}
