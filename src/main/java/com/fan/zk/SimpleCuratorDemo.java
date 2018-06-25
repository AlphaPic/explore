package com.fan.zk;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author:fanwenlong
 * @date:2018-06-13 16:17:18
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class SimpleCuratorDemo {
    public static void main(String[] args){
        RetryPolicy policy = new ExponentialBackoffRetry(1000,3);
        CuratorFramework curator = CuratorFrameworkFactory.newClient("127.0.0.1:2181",policy);

        try {
            List<String> childrenPathList = curator.getChildren().forPath("/zookeeper");
            if (childrenPathList != null){
                for (String child : childrenPathList){
                    System.out.println(child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
