package com.fan.distributedlock.impl;

import com.fan.distributedlock.config.ZookeeperConfig;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static org.apache.zookeeper.CreateMode.EPHEMERAL;
import static org.apache.zookeeper.ZooKeeper.States.CONNECTED;

/**
 * @author:fanwenlong
 * @date:2018-05-07 16:28:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component("zookeeper")
public class ZookeeperDistributedLockServiceImpl implements DistributedLockService{
    private static Logger logger = Logger.getLogger(ZookeeperDistributedLockServiceImpl.class);
    private final Object lock = new Object();
    private ZooKeeper zk;

    @Autowired
    ZookeeperConfig config;

    @Override
    public boolean tryLock() {
        System.out.println("this method do nothing.");
        return false;
    }

    @Override
    public boolean tryRelease() {
        System.out.println("this method do nothing.");
        return false;
    }

    @Override
    public boolean lock() {

        return false;
    }

    @Override
    public boolean release() {
        return false;
    }

    public boolean lock(String uid){
        synchronized (lock) {
            String path = null;
            try {
                //判断节点是否已经存在
                if (zk != null) {
                    Stat stat = zk.exists(config.Node,false);
                    //不存在则直接创建节点
                    if(stat == null){
                        path = zk.create(config.Node,uid.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,EPHEMERAL);
                    }else {//存在则判断当前的节点是不是本线程创建的
                        byte[] bytes = zk.getData(config.Node, null, null);
                        String dataString = new String(bytes);
                        if (uid.equals(dataString)) {
                            return true;
                        }
                    }
                }else {//没有节点则直接进行创建
                    zk = new ZooKeeper(config.host1, config.TimeOut, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                        }
                    });
                    path = zk.create(config.Node, uid.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                return false;
            } finally {
                //客户端可能创建成功，但是节点创建失败也算获取失败
                if (path == null || path.equals(config.Node) == false){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 不能以关闭客户端的方式来释放分布式锁
     * @param uid
     * @return
     */
    public boolean release(String uid){
        synchronized (lock) {
            if (zk == null) {//节点必须不能为空
                logger.info("node is empty:(");
                return false;
            }
            if(uid == null || uid.isEmpty()){//传入的值不能为空
                logger.info("node value can't be empty:(");
                return false;
            }
            try {
                byte[] bytes = zk.getData(config.Node, null, null);//获取节点存放的数据
                String value = new String(bytes);
                if (zk.getState() == CONNECTED && uid.equals(value)){
                    zk.delete(config.Node,-1);//通过删除节点来释放锁
                    logger.info("delete successfully:)");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean tryRelease(String methodName, String expectedValue) {
        System.out.println("this method do nothing.");
        return false;
    }

    @Override
    public boolean tryLock(String methodName, String value) {
        System.out.println("this method do nothing.");
        return false;
    }

    public boolean unsafeWriteData(String node,String data){
        if(node == null && node.isEmpty()){
            return false;
        }

        try {
            Stat stat = zk.setData(node,data.getBytes(),-1);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unsafeReadData(String node){
        if(node == null && node.isEmpty()){
            return false;
        }

        try {
            byte[] data = zk.getData(node,null,null);
            return data.length != 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean initZookeeper(String value){
        if(zk != null){
            return true;
        }
        String path = null;
        try {
            zk = new ZooKeeper(config.host1, config.TimeOut, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                }
            });

            path = zk.create(config.Node, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(zk == null || config.Node.equals(path) == false){
                return false;
            }
            return true;
        }
    }
}
