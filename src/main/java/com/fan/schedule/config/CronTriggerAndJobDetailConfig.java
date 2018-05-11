package com.fan.schedule.config;

import org.quartz.Trigger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class CronTriggerAndJobDetailConfig {
    private List<JobDetailFactoryBean> jobDetailFactoryBeans;

    private List<CronTriggerFactoryBean> cronTriggerFactoryBeans;

    private List<SimpleTriggerFactoryBean> simpleTriggerFactoryBeans;


    public CronTriggerAndJobDetailConfig() {
        this.jobDetailFactoryBeans   = new ArrayList<>();
        this.simpleTriggerFactoryBeans = new ArrayList<>();
        this.cronTriggerFactoryBeans = new ArrayList<>();
    }


    public List<JobDetailFactoryBean> getJobDetailFactoryBeans() {
        return jobDetailFactoryBeans;
    }

    public void setJobDetailFactoryBeans(List<JobDetailFactoryBean> jobDetailFactoryBeans) {
        this.jobDetailFactoryBeans = jobDetailFactoryBeans;
    }

    public List<CronTriggerFactoryBean> getCronTriggerFactoryBeans() {
        return cronTriggerFactoryBeans;
    }

    public void setCronTriggerFactoryBeans(List<CronTriggerFactoryBean> cronTriggerFactoryBeans) {
        this.cronTriggerFactoryBeans = cronTriggerFactoryBeans;
    }

    public List<SimpleTriggerFactoryBean> getSimpleTriggerFactoryBeans() {
        return simpleTriggerFactoryBeans;
    }

    public void setSimpleTriggerFactoryBeans(List<SimpleTriggerFactoryBean> simpleTriggerFactoryBeans) {
        this.simpleTriggerFactoryBeans = simpleTriggerFactoryBeans;
    }
}
