package com.fan.autowiki.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author:fanwenlong
 * @date:2018-06-11 13:34:26
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ScheduleTaskThreadPoolExecutor {
    static ScheduledExecutorService executorService;

    private ScheduleTaskThreadPoolExecutor(ScheduledExecutorService service){}

    public static ScheduleTaskThreadPoolExecutor getInstance(){
        return new ScheduleTaskThreadPoolExecutor(new ScheduledThreadPoolExecutor(4));
    }

    /**
     * 增加定时任务
     * @param runnable
     */
    public static boolean addTask(Runnable runnable,TASKTYPE tasktype){
        if (executorService == null){
            System.out.println("定时任务执行线程池为空.");
            return false;
        }
        switch (tasktype){
            case GIT:
                executorService.schedule(runnable,10, TimeUnit.MINUTES);
                break;
            case MAVEN:
                executorService.schedule(runnable,100, TimeUnit.SECONDS);
                break;
            default:
                System.out.println("不支持的任务类型");
                return false;
        }
        return true;
    }
}
