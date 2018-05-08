package com.fan.distributedlock.impl;

import com.fan.distributedlock.ConfigurationScan;
import com.fan.utils.ProcessUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author:fanwenlong
 * @date:2018-05-08 13:51:46
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component("test")
public class LockRunningTest {
    private static Logger logger = Logger.getLogger(LockRunningTest.class);

    @Autowired
    @Qualifier("mysql")
    private DistributedLockService mysqlLock;

    @Autowired
    @Qualifier("redis")
    private DistributedLockService redisLock;

    @Autowired
    @Qualifier("zookeeper")
    private DistributedLockService zkLock;


    /**
     * 通过插入和删除特定的记录来实现基于mysql的数据库分布式锁
     */
    public void lockWithMysqlByInsert(){
        String id = ProcessUtil.getRandomProcessId();
        System.out.println("-----------------------------Insert lock start-----------------------------");
        boolean isTaken = mysqlLock.lock();
        boolean isLost  = false;
        if(isTaken == true){
            logger.info("Got it by [" + id + "][" + isTaken + "]");
            isLost = mysqlLock.release();
            logger.info("Lost it by[" + id + "][" + isLost + "]");
        }
        System.out.println("-----------------------------Insert lock end-----------------------------");
    }

    /**
     * 改进型
     * 通过插入和删除特定的记录来实现基于mysql的数据库分布式锁
     */
    public void lockWithMysqlByInsertAdvance(){

    }

    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationScan.class);
        LockRunningTest service = (LockRunningTest) context.getBean("test");
        Integer SELECT = 1;
        switch (SELECT){
            /********************MYSQL*********************/
            case 1://插入方式获取分布式锁
                service.lockWithMysqlByInsert();
                break;
            case 2://改进型的插入式方式获取分布式锁
                service.lockWithMysqlByInsertAdvance();
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                logger.info("Unsupported execution!!!");
                break;
        }
    }
}
