package com.fan.multithreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author:fanwenlong
 * @date:2018-01-31 10:38:21
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:多线程写原子变量
 * @detail:
 */
public class AtomicHandle {
    private int count = 0;
    /**
     * 非安全写测试
     */
    public void unsafeWrite(){

        ExecutorService service = Executors.newFixedThreadPool(10);
        int num = 10;
        while (num-- > 0){
            service.submit(new CountThread("Thread-" + num));
        }
        System.out.println(count);
    }

    class CountThread implements Runnable {
        private String name;

        public CountThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (count < 10000){
                System.out.println(name + " is counting");
                count++;
            }
        }
    }

    public static void main(String[] args){
        new AtomicHandle().unsafeWrite();
    }
}
