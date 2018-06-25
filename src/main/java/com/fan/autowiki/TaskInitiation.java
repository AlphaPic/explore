package com.fan.autowiki;

import com.fan.autowiki.task.GitTask;
import com.fan.autowiki.task.GitVo;
import com.fan.autowiki.task.ScheduleTaskThreadPoolExecutor;
import com.fan.autowiki.task.TASKTYPE;

/**
 * @author:fanwenlong
 * @date:2018-06-11 14:34:54
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class TaskInitiation {
    private static String USERNAME = "fanwenlong";
    private static String PASSWORD = "fanwenlong1992";
    private static String ROOTDIR  = "E:\\data\\lab\\WIKITMP\\";
    private static String BRANCH   = "master";

    private static String CSA_COMMON_PROJ = "csa-common";
    private static String CSA_OTHER_PROJ   = "csa-other";
    private static String CSA_USSO_PROJ   = "csa-usso";
    private static String CSA_TICKET_PROJ   = "csa-ticket";
    private static String CSA_ROUTE_PROJ   = "csa-route";

    private static String CSA_COMMON_GIT_URL = "http://10.200.5.103/lvtu_api/csa-commons.git";
    private static String CSA_OTHER_GIT_URL = "http://10.200.5.103/lvtu_api/csa-other.git";
    private static String CSA_USSO_GIT_URL = "http://10.200.5.103/lvtu_api/csa-user-sso.git";
    private static String CSA_TICKET_GIT_URL = "http://10.200.5.103/lvtu_api/csa-ticket.git";
    private static String CSA_ROUTE_GIT_URL = "http://10.200.5.103/lvtu_api/csa-route.git";

    public static void InitGitCommand(){
        GitVo commonVo = new GitVo();
        commonVo.setUserName(USERNAME)
                .setPassword(PASSWORD)
                .setRootDir(ROOTDIR + System.currentTimeMillis())
                .setBranch(BRANCH)
                .setProject(CSA_COMMON_PROJ)
                .setUrl(CSA_COMMON_GIT_URL);

        GitVo csa_other = new GitVo();
        csa_other.setUserName(USERNAME)
                .setPassword(PASSWORD)
                .setRootDir(ROOTDIR)
                .setBranch(BRANCH)
                .setProject(CSA_OTHER_PROJ)
                .setUrl(CSA_OTHER_GIT_URL);

        GitTask commonTask = new GitTask("common",commonVo);
        GitTask otherTask  = new GitTask("csa-other",csa_other);

        ScheduleTaskThreadPoolExecutor.addTask(commonTask, TASKTYPE.GIT);
        ScheduleTaskThreadPoolExecutor.addTask(otherTask, TASKTYPE.GIT);
    }

    public static void InitMavenCommand(){

    }
}
