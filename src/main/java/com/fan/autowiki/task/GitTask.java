package com.fan.autowiki.task;

import com.fan.autowiki.InitFailedException;
import com.fan.autowiki.utils.JedisUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

/**
 * @author:fanwenlong
 * @date:2018-06-11 11:16:35
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class GitTask implements Runnable{
    /**
     * 任务名称
     */
    String taskName;

    /**
     * git操作
     */
    GitVo gitVo;

    /**
     * 高级git操作的api
     */
    Git git;

    /**
     * 执行间隔
     */
    Integer intervals;

    public GitTask(String taskName, GitVo gitVo) {
        this.taskName = taskName;
        this.gitVo = gitVo;
    }

    /**
     * 禁止使用new的方式创建对象
     * @param taskName
     * @return
     */
    public static GitTask getInstance(String taskName,GitVo gitVo){
        return new GitTask(taskName,gitVo);
    }

    /**
     * 在这个执行流程下面进行一个git流程，包括
     * 1、执行git clone操作
     * 2、执行执行分支切换
     */
    @Override
    public void run() {
        try {
            load();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * 操作前的预处理
     */
    private void loadByClone() throws InitFailedException, GitAPIException {
        JedisUtil.setStringValue("FORCED-EXECUTED","true");
        File file = new File(gitVo.getRootDir());
        //目前只能进行文件的拉取，不支持文件的pull操作
        if(!file.exists()) {
            git = Git.cloneRepository()
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitVo.getUserName(),gitVo.getPassword()))
                    .setURI(gitVo.getUrl())
                    .setDirectory(new File(gitVo.getRootDir()))
                    .setBranch(gitVo.getBranch())
                    .call();
            if (git == null)
                throw new InitFailedException("初始化git失败");
        }
        JedisUtil.setStringValue("FORCED-EXECUTED","false");
        JedisUtil.setStringValue("LAST-5-MINUTE", String.valueOf(System.currentTimeMillis()));
    }

    /**
     * git clone操作
     * 更新的操作的前提有两个
     * 1、强制执行的标志位已经被设置了
     * 2、过去5分钟内没有被执行
     * @throws GitAPIException
     * @throws InitFailedException
     */
    public void load() throws GitAPIException, InitFailedException {
        if(forcedExecuted() || executedInLast5Minute() == false) {
            loadByClone();
        }

        System.out.println("目前git clone已执行" + JedisUtil.addIntegerValue("GIT-CLONE-TIMES") + "次!!!");
    }

    //策略1，判断过去5分钟是否已经执行
    private boolean executedInLast5Minute() {
        String val = JedisUtil.getStringValue("LAST-5-MINUTE");
        if(val == null)
            return false;
        Long lastExecTime = Long.parseLong(val);
        Long curTime      = System.currentTimeMillis();
        if(curTime - lastExecTime > (5 * 60 * 1000)){
            return true;
        }

        return false;
    }

    //策略2、判断当前是否处于强制执行的状态
    private boolean forcedExecuted() {
        String val = JedisUtil.getStringValue("FORCED-EXECUTED");
        return val != null && val.isEmpty() == false && val.equals("false");
    }


    public static void main(String[] args){
        GitVo vo = new GitVo();
        vo.setUserName("fanwenlong")
                .setPassword("fanwenlong1992")
                .setBranch("coupon")
                .setRootDir("E:\\data\\lab\\WIKITMP\\" + System.currentTimeMillis() + "\\csa-other")
                .setUrl("http://10.200.5.103/lvtu_api/csa-other.git");
        try {
            GitTask.getInstance(null, vo).load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
