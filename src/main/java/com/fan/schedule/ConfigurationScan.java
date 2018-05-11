package com.fan.schedule;


import com.fan.schedule.config.QuartzConfig;
import com.fan.schedule.job.SimpleQuartzJob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {SimpleQuartzJob.class, QuartzConfig.class})
public class ConfigurationScan {
}
