package com.fan.multithreads;

import java.sql.Time;

/**
 * @author:fanwenlong
 * @date:2018-04-29 17:06:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ThreadLocalTest {
    public static void main(String[] args){
        final ThreadLocal threadLocal = new ThreadLocal();
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadLocal.set(12);
                while (true){
                    try {
                        Thread.sleep(100000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        for(int i = 0;i < 10;i++) {
            threadLocal.set(i);
            System.out.println(threadLocal.get());
        }
    }
}
