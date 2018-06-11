package com.fan.multithreads;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author:fanwenlong
 * @date:2018-06-05 14:23:40
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ConditionExplore {
    private final PriorityQueue<Integer> queue = new PriorityQueue<>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition notFull  = lock.newCondition();
    private Condition notEmpty = lock.newCondition();
    private Integer QUEUE_SIZE = 800;
    private Integer start = 0;
    private Random rand = new Random(10);

    /**
     * 生产者
     */
    class Producer implements Runnable{

        @Override
        public void run() {
            while (true){
                lock.lock();
                if(queue.size() == QUEUE_SIZE){
                    try {
                        notFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                notEmpty.signal();
                queue.offer(start++);
                System.out.println("生产者已经将start = " + start + "的数放进队列了");
                lock.unlock();
                sleepForAWhile(rand.nextInt(1));
            }
        }
    }

    /**
     * 消费者
     */
    class Consumer implements Runnable{

        @Override
        public void run() {
            while (true){
                lock.lock();
                if (queue.size() == 0){
                    try {
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                notFull.signal();
                Integer val = queue.poll();
                System.out.println("消费者已经将start = " + val + "的值移出队列了");
                lock.unlock();
                sleepForAWhile(rand.nextInt(90));
            }
        }
    }

    public void sleepForAWhile(Integer sec){
        try {
            Thread.sleep(sec * 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testProducerAndConsumer(){
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public static void main(String[] args){
        new ConditionExplore().testProducerAndConsumer();
    }
}
