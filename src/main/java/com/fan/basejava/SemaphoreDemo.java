package com.fan.basejava;

import java.util.concurrent.Semaphore;

/**
 * @author:fanwenlong
 * @date:2018-04-03 13:49:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class SemaphoreDemo {
    public static void main(String[] args){
        final Semaphore semaphore = new Semaphore(5);

        for(int i = 0;i < 20;i++){
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("current thread No:" + finalI);
                        Thread.sleep((long) (Math.random() * 10000));
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
