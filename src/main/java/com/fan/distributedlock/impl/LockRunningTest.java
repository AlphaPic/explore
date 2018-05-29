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
    private ZookeeperDistributedLockServiceImpl zkLock;


    /**
     * 通过插入和删除特定的记录来实现基于mysql的数据库分布式锁
     */
    public void lockWithMysqlByInsert(){
        String id = ProcessUtil.getRandomProcessId();
        System.out.println("-----------------------------start-----------------------------");
        boolean isTaken = mysqlLock.lock();
        boolean isLost  = false;
        System.out.println("客户端[" + id + "]获取锁" + (isTaken ? "成功" : "失败") + "");
        if(isTaken == true){
            DoSomethingThatWillCostSomeTime(100);
            isLost = mysqlLock.release();
        }
        System.out.println("客户端[" + id + "]释放锁" + (isLost ? "成功" : "失败") + "");
        System.out.println("-----------------------------end-----------------------------");
    }

    /**
     * 改进型
     * 通过插入和删除特定的记录来实现基于mysql的数据库分布式锁
     */
    public void lockWithMysqlByInsertAdvance(String lockName){
        String methodName = lockName;
        String desc       = ProcessUtil.getRandomProcessId();
        Class clazz = mysqlLock.getClass();
        try {
            Method lock     = clazz.getMethod("lockAdvance",String.class,String.class);
            Method release  = clazz.getMethod("releaseAdvance",String.class,String.class);

            System.out.println("-----------------------------start-----------------------------");
            boolean isTaken = (boolean) lock.invoke(mysqlLock,methodName,desc);
            System.out.println("客户端[" + desc + "]获取锁" + (isTaken ? "成功" : "失败") + "");
            boolean isLost = false;
            if(isTaken){
                DoSomethingThatWillCostSomeTime(100);
                isLost = (boolean) release.invoke(mysqlLock,methodName,desc);
            }
            System.out.println("客户端[" + desc + "]释放锁" + (isLost ? "成功" : "失败") + "");
            System.out.println("-----------------------------end-----------------------------");
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
                System.out.println("Thread[" + threadId + "],hold lock[" + methodName + "]" + (isLocked == true ? "successfully:)" : "failed :("));
                if(isLocked){
                    Thread.sleep(10000);
                    isReleased = (boolean) release.invoke(mysqlLock,methodName);
                    System.out.println("Thread[" + threadId + "],release lock[" + methodName + "]" + (isReleased == true ? "successfully:)" : "failed :("));
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
    public void lockWithRedisSingleNode() throws InterruptedException {
        boolean lock = redisLock.lock();
        System.out.println("获取redis分布式锁[单个节点]" + (lock ? "成功:)" : "失败:("));
        if(lock){
            DoSomethingThatWillCostSomeTime(20);
            System.out.println("释放redis分布式锁[多个节点]" + (redisLock.release() ? "成功:)" : "失败:("));
        }
    }

    /**
     * 使用多个Redis节点实现分布式锁
     */
    public void lockWithRedisMultiNode(){
        try {
            Class clazz = redisLock.getClass();
            Method lock = clazz.getMethod("lockWithMultiServer", String.class);
            Method release = clazz.getMethod("releaseWithMultiServer", String.class);
            String id = ProcessUtil.getRandomProcessId();

            boolean res = (boolean) lock.invoke(redisLock,id);
            if(res == true){
                logger.info("Got distributed lock");
                DoSomethingThatWillCostSomeTime(5);
                boolean releaseFlag = (boolean) release.invoke(redisLock,id);
                logger.info("Release distributed lock " + (releaseFlag ? "successful:)" : "failed:("));
            }
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    /**
     * 使用Redisson实现分布式锁
     */
    public void lockWithRedisByRedisson(){

    }

    /**
     * 使用zookeeper实现分布式锁
     */
    public void lockWithZookeeper(){
        String id = ProcessUtil.getRandomProcessId();

        boolean res = zkLock.lock(id);
        if(res){
            logger.info("Got zk distributed lock!!!");
            DoSomethingThatWillCostSomeTime(5);
            logger.info("Release zk distributed lock " + (zkLock.release(id) ? "successfully:)" : "failed:("));
        }
    }

    /**
     * 使用curator来实现分布式锁
     */
    public void lockWithZookeeperCurator(){

    }

    /**
     * 做些浪费时间的事情
     */
    private void DoSomethingThatWillCostSomeTime(Integer seconds){
        try{
            Thread.sleep(1000 * seconds);
        }catch (Exception e){

        }
    }





    public static void main(String[] args) throws InterruptedException {
        if(args.length < 1){
            System.out.println("参数长度必须大于1");
            return;
        }
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationScan.class);
        LockRunningTest service = (LockRunningTest) context.getBean("test");
        Integer SELECT = Integer.valueOf(args[0]);
        switch (SELECT){
            /********************MYSQL*********************/
            case 1://插入方式获取分布式锁
                service.lockWithMysqlByInsert();
                break;
            case 2://改进型的插入式方式获取分布式锁
                if (args.length != 2){
                    System.out.println("无法使用拓展的锁，因为传入参数个数小于2");
                    return;
                }
                service.lockWithMysqlByInsertAdvance(args[1]);
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
                service.lockWithZookeeper();
                break;
            case 8://使用分布式锁的中间件curator
                service.lockWithZookeeperCurator();
                break;
            default:
                logger.info("Unsupported execution!!!");
                break;
        }
    }
}
