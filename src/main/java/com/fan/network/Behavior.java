package com.fan.network;

import com.fan.utils.PatternUtils;

import java.util.regex.Pattern;

/**
 * @author:fanwenlong
 * @date:2018-03-15 15:01:54
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public enum Behavior {
    LOGIN("login"),
    EXIT("exit"),
    UPLOAD("upload"),
    DOWNLOAD("download"),
    HELP("help"),
    UNKNOWN("unknown");

    private String name;

    Behavior(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 判断行为类型
     * @param cmd
     * @return
     */
    public static Behavior getBehavior(String cmd){
        if(cmd == null || cmd.isEmpty()){
            return UNKNOWN;
        }
        String[] types = PatternUtils.pattern.split(cmd.trim());
        if(types.length < 1){
            return UNKNOWN;
        }
        String type = types[0];
        if("login".equalsIgnoreCase(type)){
            return LOGIN;
        }else if("upload".equalsIgnoreCase(type)){
            return UPLOAD;
        }else if("download".equalsIgnoreCase(type)){
            return DOWNLOAD;
        }else {
            return UNKNOWN;
        }
    }
}
