package com.fan.distributedlock.config;

import org.springframework.stereotype.Component;

/**
 * @author:fanwenlong
 * @date:2018-05-14 16:55:39
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component
public class ZookeeperConfig {
    public final String host1 = "10.112.2.88:2181";
    public final String host2 = "10.112.2.63:2181";
    public final String host3 = "10.112.2.140:2181";

    public final int TimeOut = 50000;

    public final String Node = "/node";
}
