package com.fan.distributedlock.config.mapper;

import com.fan.distributedlock.config.bean.MethodLock;
import org.apache.ibatis.annotations.*;

/**
 * @author:fanwenlong
 * @date:2018-05-07 14:12:40
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:methodLock的相关操作
 * @detail:
 */
@Mapper
public interface MethodNameMapper {
    /**
     * 选择方法名称
     * @param methodName
     * @return
     */
    @Select("select * from methodLock where method_name = #{methodName}")
    MethodLock getMethodName(@Param("methodName") String methodName);

    /**
     * 插入方法名称记录
     * @param methodName
     * @param description
     * @return
     */
    @Insert("insert into methodLock (method_name,description) values (#{methodName},#{description})")
    Integer insertMethodName(@Param("methodName")   String methodName,
                             @Param("description")  String description);

    /**
     * 删除方法名称记录
     * @param methodName
     * @return
     */
    @Delete("delete from methodLock where method_name = #{methodName}")
    Boolean deleteMethodName(@Param("methodName") String methodName);

    /**
     * 获取数据库的行锁
     * @param methodName
     * @return
     */
    @Select("select from methodLock where method_name = #{methodName} for update")
    MethodLock selectForALock(@Param("methodName") String methodName);
}
