package com.fan.multithreads;

/**
 * @author:fanwenlong
 * @date:2018-01-18 20:31:08
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:Thread.join()使用来等待其它线程完成之后才继续执行的线程
 * @detail:
 */
public class ThreadJoinExplore {
    public static void main(String[] args){
        Thread previous = Thread.currentThread();
        for(int i = 0;i < 10;i++){
            Thread recursiveThread = new Thread(new RecursiveThread(previous),"thread-" + i);
            recursiveThread.start();
            previous = recursiveThread;
        }
    }

    /**
     * 一个等待其它线程完成的递归线程
     */
    static class RecursiveThread implements Runnable{
        private Thread previous;

        public RecursiveThread(Thread previous){
            this.previous = previous;
        }

        @Override
        public void run() {
            try {
                previous.join();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 已经结束!");
        }
    }
}
