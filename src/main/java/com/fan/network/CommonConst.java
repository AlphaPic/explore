package com.fan.network;

import java.util.regex.Pattern;

/**
 * @author:fanwenlong
 * @date:2018-03-19 14:22:50
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public interface CommonConst {
    public static final Pattern ipPattern = Pattern.compile("^(((\\\\d{1,2})|(1\\\\d{2})|(2[2-4]\\\\d)|(25[0-5]))\\\\.){3}((\\\\d{1,2})|(1\\\\d{2})|(2[2-4]\\\\d)|(25[0-5]))$");

    public static final Integer heartPort = 22222;                              /** 心跳套接字端口 */
}
