package com.fan.autowiki;

import java.io.Serializable;

/**
 * @author:fanwenlong
 * @date:2018-06-11 11:08:58
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class GitVo implements Serializable{
    private static final long serialVersionUID = -9190527525351408727L;

    /**
     * 编译Git根目录,这个目录是为了进行代码的拉取
     */
    private String RootDir;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 项目url
     */
    private String url;

    /**
     * 项目分支
     */
    private String branch;

    /**
     * 项目名称
     */
    private String project;

    public String getRootDir() {
        return RootDir;
    }

    public GitVo setRootDir(String rootDir) {
        RootDir = rootDir;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public GitVo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public GitVo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public GitVo setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public GitVo setBranch(String branch) {
        this.branch = branch;
        return this;
    }

    public String getProject() {
        return project;
    }

    public GitVo setProject(String project) {
        this.project = project;
        return this;
    }
}
