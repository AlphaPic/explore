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
        final ThreadLocal[] threadLocal = new ThreadLocal[20];
        for (int i = 0;i < threadLocal.length;i++){
            threadLocal[i] = new ThreadLocal();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int k = 0;k < 10;k++) {
                    for (int i = 0;i < threadLocal.length;i++){
                        threadLocal[i].set(k * (threadLocal.length - i));
                    }
                }
            }
        }).start();
    }
}
