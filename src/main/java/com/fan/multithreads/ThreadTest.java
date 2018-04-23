package com.fan.multithreads;

import java.util.concurrent.TimeUnit;

/**
 * @author:fanwenlong
 * @date:2018-04-04 14:36:16
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ThreadTest {

    public static void interruptedTest() throws InterruptedException {
        Runner one = new Runner();
        Thread countThread = new Thread(one,"CountThread");
        countThread.start();

        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();
        Runner two = new Runner();
        countThread = new Thread(two,"CountThread");
        countThread.start();
        TimeUnit.SECONDS.sleep(1);
        two.cancel();
    }

    private static final ThreadLocal<Long> TIME_THREADLOCAL =  new ThreadLocal<Long>();
    protected Long initialValue(){
        return System.currentTimeMillis();
    }

    public static final void begin(){
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    public static final long end(){
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    public static void main(String[] args) throws InterruptedException {
        begin();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Cost:" + end());
    }

    private static class Runner implements Runnable{
        private long i;
        private volatile boolean on = true;
        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()){
                i++;
            }
            System.out.println("Count i = " + i);
        }

        public void cancel(){
            on = false;
        }
    }
}
