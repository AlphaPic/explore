package com.fan.schedule.config;

import com.fan.schedule.job.SimpleQuartzJob;
import org.apache.ibatis.annotations.Param;
import org.elasticsearch.common.inject.Singleton;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;

@Component
public class QuartzConfig {
    private final String SIMPLE_CRON_EXPRESSION = "*/5 * * * * ?";

    @Bean
    public CronTriggerAndJobDetailConfig config() throws ParseException {
        CronTriggerAndJobDetailConfig config = new CronTriggerAndJobDetailConfig();
        JobDataMap map = new JobDataMap();

        //设置JobDetail
        JobDetailFactoryBean jobDetailFactoryBean = createJobDetailFactoryBean(
                "gropu1",
                true,
                "job1",
                map,
                SimpleQuartzJob.class);

        //设置Cron Trigger
        CronTriggerFactoryBean cronTriggerFactoryBean = createCronTriggerFactoryBean(
                "group1",
                "trigger2",
                SIMPLE_CRON_EXPRESSION,
                jobDetailFactoryBean.getObject());

        //设置 Simple trigger
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = createSimpleTriggerFactoryBean(
                "group1",
                "trigger1",
                5,
                new Date(),
                5,
                1000,
                jobDetailFactoryBean.getObject());


        //将设置添加到链表之中
        config.getCronTriggerFactoryBeans().add(cronTriggerFactoryBean);
        config.getJobDetailFactoryBeans().add(jobDetailFactoryBean);
        config.getSimpleTriggerFactoryBeans().add(simpleTriggerFactoryBean);

        return config;
    }

    /**
     * 设置调度的任务
     * @param group
     * @param durability
     * @param name
     * @param map
     * @param clazz
     * @return
     */
    private JobDetailFactoryBean createJobDetailFactoryBean(String group,boolean durability,String name,JobDataMap map,Class clazz){
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setGroup(group);
        jobDetailFactoryBean.setDurability(durability);
        jobDetailFactoryBean.setName(name);
        jobDetailFactoryBean.setJobDataMap(map);
        jobDetailFactoryBean.setJobClass(clazz);
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean;
    }

    /**
     *
     * 设置simple trigger
     * @param group
     * @param priority
     * @param name
     * @param date
     * @param repeatCount
     * @param repeatInterval
     * @param jobDetail
     * @return
     */
    private SimpleTriggerFactoryBean createSimpleTriggerFactoryBean(String group,String name,int priority,Date date,int repeatCount,long repeatInterval,JobDetail jobDetail){
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setName(name);
        simpleTriggerFactoryBean.setGroup(group);
        simpleTriggerFactoryBean.setPriority(priority);
        simpleTriggerFactoryBean.setStartTime(date);
        simpleTriggerFactoryBean.setRepeatCount(repeatCount);
        simpleTriggerFactoryBean.setRepeatInterval(repeatInterval);
        simpleTriggerFactoryBean.setJobDetail(jobDetail);
        simpleTriggerFactoryBean.afterPropertiesSet();
        return simpleTriggerFactoryBean;
    }

    /**
     * 创建 cron trigger
     * @param group
     * @param name
     * @param cronExpression
     * @param jobDetail
     * @return
     * @throws ParseException
     */
    private CronTriggerFactoryBean createCronTriggerFactoryBean(String group,String name,String cronExpression,JobDetail jobDetail) throws ParseException {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression(cronExpression);
        cronTriggerFactoryBean.setName(name);
        cronTriggerFactoryBean.setGroup(group);
        cronTriggerFactoryBean.afterPropertiesSet();
        return cronTriggerFactoryBean;
    }



    @Bean(name = "schedule")
    public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerAndJobDetailConfig config){
        SchedulerFactoryBean sched = new SchedulerFactoryBean();

        List<JobDetailFactoryBean> jobDetailFactoryBeans            = config.getJobDetailFactoryBeans();
        List<CronTriggerFactoryBean> cronTriggerFactoryBeans        = config.getCronTriggerFactoryBeans();
        List<SimpleTriggerFactoryBean> simpleTriggerFactoryBeans    = config.getSimpleTriggerFactoryBeans();

        //设置调度器的job
        if(
                jobDetailFactoryBeans       == null   ||    jobDetailFactoryBeans.size()        != 1 ||
                cronTriggerFactoryBeans     == null   ||    cronTriggerFactoryBeans.size()      != 1 ||
                simpleTriggerFactoryBeans   == null   ||    simpleTriggerFactoryBeans.size()    != 1){
            return null;
        }
        sched.setJobDetails(jobDetailFactoryBeans.get(0).getObject());
        sched.setTriggers(cronTriggerFactoryBeans.get(0).getObject(),
                simpleTriggerFactoryBeans.get(0).getObject());

        return sched;
    }
}
