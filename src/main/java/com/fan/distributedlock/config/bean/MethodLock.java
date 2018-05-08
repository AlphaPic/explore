package com.fan.distributedlock.config.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 * @author:fanwenlong
 * @date:2018-05-07 14:17:53
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class MethodLock implements Serializable{
    private static final long serialVersionUID = -5477745799914119082L;
    private Integer id;

    private String method_name;

    private String desc;

    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod_name() {
        return method_name;
    }

    public void setMethod_name(String method_name) {
        this.method_name = method_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
