package com.fan.autowiki.task;

/**
 * @author:fanwenlong
 * @date:2018-06-11 11:17:07
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class MavenTask implements Runnable{
    /**
     * 任务名称
     */
    String taskName;

    /**
     * 执行间隔
     */
    Integer intervals;

    private MavenTask(String taskName) {
        this.taskName = taskName;
    }

    public static MavenTask getInstance(String taskName){
        return new MavenTask(taskName);
    }

    @Override
    public void run() {
    }
}
