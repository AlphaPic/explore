package com.fan.distributedlock;

import com.fan.distributedlock.config.MybatisConfig;
import com.fan.distributedlock.impl.MysqlDistributedLockServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author:fanwenlong
 * @date:2018-05-07 16:58:00
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Configuration
@ComponentScan(basePackageClasses = {MybatisConfig.class, MysqlDistributedLockServiceImpl.class})
public class ConfigurationScan {
}
