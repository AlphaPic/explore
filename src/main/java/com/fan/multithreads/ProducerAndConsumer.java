package com.fan.multithreads;

import java.util.concurrent.CountDownLatch;

/**
 * @author:fanwenlong
 * @date:2018-04-29 17:53:25
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ProducerAndConsumer {

    private final Object lock = new Object();
    private volatile boolean create = false;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        ProducerAndConsumer demo = new ProducerAndConsumer();
        Thread producer = new Thread(demo.new ProducerThread());
        Thread consumer = new Thread(demo.new ConsumerThread());

        consumer.start();
        producer.start();
        Thread.sleep(1000);
    }

    private void acquireNotNull(Object...objects) throws Exception {
        for(Object object : objects){
            if(object == null)
                throw new Exception("object can't be empty.");
        }
    }

    class ProducerThread implements Runnable{


        @Override
        public void run() {
            try {
//                acquireNotNull(lock,countDownLatch);
                Thread.sleep(100);
                System.out.println("producer started yet.");

                synchronized (lock){
                    create = true;
                    lock.notify();
                }

                System.out.println("producer end yet.");
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    class ConsumerThread implements Runnable{

        @Override
        public void run() {
            try {
//                acquireNotNull(lock,countDownLatch);
//                Thread.sleep(100);
                synchronized (lock) {
                    System.out.println("consumer start yet.");
                    while (!create) {
                        lock.wait();
                    }
                }
                System.out.println("consumer end yet.");


            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}
