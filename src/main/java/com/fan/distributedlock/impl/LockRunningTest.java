package com.fan.distributedlock.impl;

import com.fan.distributedlock.ConfigurationScan;
import com.fan.utils.ProcessUtil;
import com.sun.jmx.snmp.tasks.ThreadService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
        logger.info("Got it by [" + id + "][" + isTaken + "]");
        if(isTaken == true){
            isLost = mysqlLock.release();
        }
        logger.info("Lost it by[" + id + "][" + isLost + "]");
        System.out.println("-----------------------------Insert lock end-----------------------------");
    }

    /**
     * 改进型
     * 通过插入和删除特定的记录来实现基于mysql的数据库分布式锁
     */
    public void lockWithMysqlByInsertAdvance(){
        String methodName = "lily";
        String desc       = ProcessUtil.getRandomProcessId();
        Class clazz = mysqlLock.getClass();
        try {
            Method lock     = clazz.getMethod("lockAdvance",String.class,String.class);
            Method release  = clazz.getMethod("releaseAdvance",String.class,String.class);

            System.out.println("---------------[ADVANCE]--------------Insert lock start-----------------------------");
            boolean isTaken = (boolean) lock.invoke(mysqlLock,methodName,desc);
            logger.info("Got it by[" + desc + "][" + isTaken + "]");
            boolean isLost = false;
            if(isTaken){
                isLost = (boolean) release.invoke(mysqlLock,methodName,desc);
            }
            logger.info("Lost it by[" + desc + "][" + isLost + "]");
            System.out.println("---------------[ADVANCE]--------------Insert lock end-----------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 工作线程
     */
    class WorkThread implements Runnable{
        private Long threadId;
        private CountDownLatch latch;
        private String methodName;

        public WorkThread(Long threadId, CountDownLatch latch, String methodName) {
            this.threadId = threadId;
            this.latch = latch;
            this.methodName = methodName;
        }

        @Override
        public void run() {
            try {
                latch.await();
                Class clazz     = mysqlLock.getClass();
                Method lock     = clazz.getMethod("lockWithSelection",String.class);
                Method release  = clazz.getMethod("releaseWithSelection",String.class);
                boolean isLocked    = (boolean) lock.invoke(mysqlLock,methodName);
                boolean isReleased  = false;
                logger.info("Thread[" + threadId + "],hold lock[" + methodName + "]" + (isLocked == true ? "successfully:)" : "failed :("));
                if(isLocked){
                    Thread.sleep(10000);
                    isReleased = (boolean) release.invoke(mysqlLock,methodName);
                    logger.info("Thread[" + threadId + "],release lock[" + methodName + "]" + (isReleased == true ? "successfully:)" : "failed :("));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 通过使用select for update的方式来获取行锁
     */
    public void lineLockWithMysqlBySelectForUpdate(){
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final CountDownLatch latch = new CountDownLatch(1);
        WorkThread thread1 = new WorkThread(1001L,latch,"lily");
        WorkThread thread2 = new WorkThread(1002L,latch,"lily");
        executor.submit(thread1);
        executor.submit(thread2);
        latch.countDown();

        try{
            Thread.sleep(20000);
        }catch (Exception e){
        }
        executor.shutdown();
    }

    /**
     * 使用单个Redis节点实现分布式锁
     */
    public void lockWithRedisSingleNode(){
        redisLock.tryLock();
    }

    /**
     * 使用多个Redis节点实现分布式锁
     */
    public void lockWithRedisMultiNode(){

    }

    /**
     * 使用Redisson实现分布式锁
     */
    public void lockWithRedisByRedisson(){

    }





    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationScan.class);
        LockRunningTest service = (LockRunningTest) context.getBean("test");
        Integer SELECT = 9;
        switch (SELECT){
            /********************MYSQL*********************/
            case 1://插入方式获取分布式锁
                service.lockWithMysqlByInsert();
                break;
            case 2://改进型的插入式方式获取分布式锁
                service.lockWithMysqlByInsertAdvance();
                break;
            case 3://使用select for update 来获取分布式锁(行)
                service.lineLockWithMysqlBySelectForUpdate();
                break;
            case 4://使用单个节点Redis实现分布式锁
                service.lockWithRedisSingleNode();
                break;
            case 5://使用多个节点Redis实现分布式锁
                service.lockWithRedisSingleNode();
                break;
            case 6://使用Redisson实现分布式锁
                service.lockWithRedisByRedisson();
                break;
            case 7://使用zk实现分布式锁
                break;
            case 8://使用分布式锁的中间件
                break;
            default:
                logger.info("Unsupported execution!!!");
                break;
        }
    }
}
