package com.fan.network;

/**
 * @author:fanwenlong
 * @date:2018-03-15 12:33:17
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public enum CodeInfo {

    LOGIN_SUCCESS("0x0000"),             //登录成功
    LOGOUT_SUCCESS("0x0001"),            //登出成功
    UPLOAD_STARTED("0x0002"),            //上传开始
    UPLOAD_FINISHED("0x0003"),           //上传完成
    DOWNLOAD_STARTED("0x0004"),          //下载开始
    DOWNLOAD_FINISHED("0x0005"),         //下载完成

    /** 异常码,0x7000~0x7FFFF */
    FILE_NOT_FOUND("0x7000"),            //文件没有找到
    UNDEFINED_COMMAND("0x7001"),         //未定义指令
    NOT_LOGIN_YET("0x7002"),             //未登录

    /** 错误码 */
    ERROR_CODE("0xffff"),                //错误码

    /** 检测码 */
    HEART_PACKAGE_START("0x4000"),      //心跳开始
    HEART_PACKAGE_END("0x4001");        //心跳结束

    private String name;


    CodeInfo(String s) {
        this.name = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
