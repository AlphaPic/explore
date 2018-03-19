package com.fan.network;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author:fanwenlong
 * @date:2018-03-16 15:49:44
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class OperationVo implements Serializable{
    private static final long serialVersionUID = 8209374637388588008L;

    /**
     * 操作行为
     */
    private Behavior behavior;

    /**
     * 路径
     */
    private String path;

    /**
     * 文件
     */
    private String file;

    /**
     * 处理之后的命令
     */
    private String handledCmd;

    /**
     * 登录令牌
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHandledCmd() {
        return handledCmd;
    }

    public OperationVo setHandledCmd(String handledCmd) {
        this.handledCmd = handledCmd;
        return this;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public OperationVo setBehavior(Behavior behavior) {
        this.behavior = behavior;
        return this;
    }

    public String getPath() {
        return path;
    }

    public OperationVo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getFile() {
        return file;
    }

    public OperationVo setFile(String file) {
        this.file = file;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"behavior\":")
                .append(behavior);
        sb.append(",\"path\":\"")
                .append(path).append('\"');
        sb.append(",\"file\":\"")
                .append(file).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public static OperationVo getOperationVo(String cmd){
        if(cmd == null || cmd.isEmpty()){
            return null;
        }
        OperationVo operation = new OperationVo();
        Behavior behavior = Behavior.getBehavior(cmd);
        Pattern pattern = Pattern.compile("[ ]+");
        String[] commands = pattern.split(cmd);
        switch (behavior){
            case LOGIN:
                break;
            case DOWNLOAD:
                if(commands.length == 3){
                    operation.setToken(commands[1]);
                    operation.setFile(commands[2]);
                    return operation;
                }
                return null;
            case UPLOAD:
                break;
            case UNKNOWN:
            default:
                break;
        }
        return operation;
    }
}
