package com.fan.multithreads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author:fanwenlong
 * @date:2018-04-08 14:19:20
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class CountTask extends RecursiveTask<Integer>{

    private static final int THREAD_HOLD = 2;
    private int start;
    private int end;

    public CountTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        Integer sum = 0;
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            for(int i = start;i <= end;i++) {
                sum += i;
            }
        }else {
            int middle = (end + start) / 2;
            CountTask leftTask  = new CountTask(start,middle);
            CountTask rightTask = new CountTask(middle + 1,end);
            leftTask.fork();
            rightTask.fork();

            int leftResult  = leftTask.join();
            int rightResult = rightTask.join();

            sum = leftResult + rightResult;
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(1,1000);

        Future<Integer> result = forkJoinPool.submit(task);

        System.out.println(result.get());
    }
}
