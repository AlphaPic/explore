package com.fan.distributedlock.impl;

import com.fan.distributedlock.config.bean.MethodLock;
import com.fan.distributedlock.config.mapper.MethodNameMapper;
import com.fan.distributedlock.consts.METHOD_NAME_PERMISSION;
import com.fan.utils.ProcessUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author:fanwenlong
 * @date:2018-05-07 13:59:57
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
@Component("mysql")
public class MysqlDistributedLockServiceImpl implements DistributedLockService {
    private static Logger logger = Logger.getLogger(MysqlDistributedLockServiceImpl.class);
    @Autowired
    private SqlSessionFactory factory;

    @Autowired
    private SqlSession sqlSession;

    @Autowired
    private DataSourceTransactionManager manager;


    private TransactionStatus status;

    /**
     * 使用同步块是为了在同一程序中不同线程可以独享这个factory
     * @return
     */
    @Override
    public boolean tryLock() {
        synchronized (factory) {
            int res = 0;
            SqlSession session = factory.openSession();
            try {
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                res = mapper.insertMethodName("lock1","lock1");
                session.commit();
            } catch (Exception e){

            }finally {
                session.close();
                return res == 1 ? true : false;
            }
        }
    }

    @Override
    public boolean tryLock(String methodName,String value){
        synchronized (factory){
            int res = 0;
            SqlSession session = factory.openSession();
            try{
                if(METHOD_NAME_PERMISSION.isNameBeenAllowed(methodName) == false){
                    return false;
                }
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                res = mapper.insertMethodName(methodName,value);
                //commit
                session.commit();
            }catch (Exception e){}finally {
                if (session != null){
                    session.close();
                }
                return res == 1 ? true : false;
            }
        }
    }

    @Override
    public boolean tryRelease() {
        return false;
    }

    @Override
    public boolean tryRelease(String methodName,String expectedValue) {
        synchronized (factory) {
            SqlSession session = factory.openSession();
            Boolean res = false;
            try{
                MethodNameMapper mapper = session.getMapper(MethodNameMapper.class);
                MethodLock lock = mapper.getMethodName(methodName);
                if(lock != null && lock.getDescription().equals(expectedValue)) {
                    res = mapper.deleteMethodName(methodName);
                    session.commit();
                }
            }catch (Exception e){
            }finally {
                if(session != null){
                    session.close();
                }
                return res;
            }
        }
    }

    @Override
    public boolean lock(){
        int count = 5;
        while (count-- > 0){
            if(tryLock() == true){
                return true;
            }
            try {
                System.out.println("failed for " + (5 - count) + " times.");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean release(){
        return tryRelease("lock1","lock1");
    }

    /**
     * 拓展的获取锁
     * @return
     */
    public boolean lockAdvance(String methodName,String desc){
        int count = 5;
        while (count-- > 0){
            if(tryLock(methodName,desc) == true){
                return true;
            }
            try {
                System.out.println("failed for " + (5 - count) + " times.");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 拓展的释放锁
     * @return
     */
    public boolean releaseAdvance(String methodName,String desc){
        return tryRelease(methodName,desc);
    }

    /**
     * 使用select...for update来进行加锁
     * @param methodName
     * @return
     */
    public boolean lockWithSelection(String methodName) throws SQLException {

        if(METHOD_NAME_PERMISSION.isNameBeenAllowed(methodName) == false){
            return false;
        }
        synchronized (manager){
            //关闭自动提交
            Connection connection = sqlSession.getConnection();
            if(connection.getAutoCommit()){
                sqlSession.getConnection().setAutoCommit(false);
            }
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
            status = manager.getTransaction(def);


            //
            try {
                MethodNameMapper mapper = sqlSession.getMapper(MethodNameMapper.class);
                MethodLock lock = mapper.selectForALock(methodName);
                if (lock == null) {
                    return false;
                }
            }catch (Exception e){
                manager.rollback(status);
            }
        }

        return true;
    }

    /**
     * 使用commit来进行解锁
     * @param methodName
     * @return
     */
    public boolean releaseWithSelection(String methodName){
        if(METHOD_NAME_PERMISSION.isNameBeenAllowed(methodName) == false){
            return false;
        }

        synchronized (manager){
            manager.commit(status);
        }

        return true;
    }
}
