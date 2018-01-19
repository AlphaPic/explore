package com.fan.multithreads;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author:fanwenlong
 * @date:2018-01-18 19:36:47
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:等待和通知机制
 * @detail:
 */
public class WaitAndNotifyExplore {

    static ThreadLocal threadLocal = new ThreadLocal();
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new WaitThread(),"waitThread");
        waitThread.start();
        Thread.sleep(1000);
        Thread notifyThread = new Thread(new NotifyThread(),"notifyThread");
        notifyThread.start();
    }

    /** 等待线程 */
    static class WaitThread implements Runnable{

        @Override
        public void run() {
            synchronized (lock){
                System.out.println(Thread.currentThread().getName() + " hold lock at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                try {
                    /** 调用对象的wait表明了对使用当前对象作为监视器的的锁被释放，可以被其它的线程获取并使用 */
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " hold lock again at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    /** 通知线程 */
    static class NotifyThread implements Runnable{

        @Override
        public void run() {
            synchronized (lock){
                System.out.println(Thread.currentThread().getName() + " hold lock at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                /** 唤醒其它的线程处于同步队列状态，但是本身还未释放锁 */
                lock.notifyAll();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            synchronized (lock){
                System.out.println(Thread.currentThread().getName() + " hold lock again at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }
}
