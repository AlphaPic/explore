package com.fan.schedule;

import com.fan.schedule.job.SimpleQuartzJob;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.applet.AppletContext;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzTest {
    public static void main(String[] args) throws SchedulerException {
//        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
//
//        Scheduler sched = schedFact.getScheduler();
//
//        sched.start();
//
//        // define the job and tie it to our HelloJob class
//        JobDetail job = newJob(SimpleQuartzJob.class)
//                .withIdentity("myJob", "group1")
//                .build();
//
//        // Trigger the job to run now, and then every 40 seconds
//        Trigger trigger = newTrigger()
//                .withIdentity("myTrigger", "group1")
//                .startNow()
//                .withSchedule(simpleSchedule()
//                        .withIntervalInSeconds(1)
//                        .repeatForever())
//                .build();
//
//        // Tell quartz to schedule the job using our trigger
//        sched.scheduleJob(job, trigger);
        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationScan.class);
    }
}
