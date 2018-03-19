package com.fan.network;

import java.io.Serializable;
import java.util.regex.Matcher;

/**
 * @author:fanwenlong
 * @date:2018-03-19 13:49:54
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ServerInfo implements Serializable{
    private static final long serialVersionUID = -5274495384367774369L;
    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 是否在线
     */
    private Boolean isOnline;

    /**
     * 平均延迟时间
     */
    private Long averageDelayTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public Long getAverageDelayTime() {
        return averageDelayTime;
    }

    public void setAverageDelayTime(Long averageDelayTime) {
        this.averageDelayTime = averageDelayTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"ip\":\"")
                .append(ip).append('\"');
        sb.append(",\"port\":")
                .append(port);
        sb.append(",\"isOnline\":")
                .append(isOnline);
        sb.append(",\"averageDelayTime\":")
                .append(averageDelayTime);
        sb.append('}');
        return sb.toString();
    }

    public static boolean isServerIllegal(ServerInfo info){
        String  ip   = info.getIp();
        Integer port = info.getPort();
        if(CommonConst.ipPattern.matcher(ip).matches() == false){
            return false;
        }
        if(port > 65535 || port < 8888){
            return false;
        }
        return true;
    }
}
