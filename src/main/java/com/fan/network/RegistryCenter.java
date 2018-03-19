package com.fan.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author:fanwenlong
 * @date:2018-03-19 11:51:15
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:注册中心
 */
public class RegistryCenter {

    /**
     * 服务器列表
     */
    private static List<ServerInfo> serverInfoList;

    /**
     * 服务器列表的最大值
     */
    private static Integer maxNumOfServerList;

    static {
        serverInfoList     = new ArrayList<ServerInfo>(20);
        maxNumOfServerList = 100;
    }

    /**
     * 添加服务器地址
     * @return
     */
    public boolean addServerAddress(ServerInfo serverInfo){
        if(ServerInfo.isServerIllegal(serverInfo) == false){
            System.out.println("ip or port isn't illegal.(ip = " + serverInfo.getIp() + ",port = " + serverInfo.getPort() + ").");
            return false;
        }
        String ip    = serverInfo.getIp();
        Integer port = serverInfo.getPort();
        synchronized (serverInfoList) {
            if (serverInfoList == null) {
                serverInfoList = new ArrayList<ServerInfo>(20);
            }
            //count limit
            if (serverInfoList.size() == maxNumOfServerList) {
                System.out.println("server number has reached max number of limit.");
                return false;
            }
            //search
            Iterator iterator = serverInfoList.iterator();
            while (iterator.hasNext()) {
                ServerInfo server = (ServerInfo) iterator.next();
                if (ip.equals(server.getIp()) && port.equals(server.getPort())) {
                    System.out.println("server is already in server list.");
                    return false;
                }
            }

            //add server info to server list
            serverInfoList.add(serverInfo);
            System.out.println("add server successfully! :)");
        }
        return true;
    }

    /**
     * get server list
     * @return
     */
    public void findServerList(Socket client){
        if(client == null){
            System.out.println("client is null");
            return;
        }
        synchronized (serverInfoList){

        }
    }

    /**
     * refresh server list
     */
    protected void refreshServerList(){
        synchronized (serverInfoList){
            if(serverInfoList == null || serverInfoList.isEmpty()){
                return;
            }
            //send heart pkg to each server
            for(ServerInfo info : serverInfoList){
                sendHeartPkg(info);
            }
        }
    }

    /**
     * send heart package to server to check if they are still connected
     * @param serverInfo
     * @return
     */
    private void sendHeartPkg(ServerInfo serverInfo){
        Socket socket = new Socket();
        Long[] connectTime = new Long[3];   //connect and write 3 times
        Integer t = 0;
        for(int i = 0;i < 3;i++){
            long start = System.currentTimeMillis();
            try {
                //connect in 3 seconds
                socket.connect(new InetSocketAddress(serverInfo.getIp(), serverInfo.getPort()),3000);
                if(socket != null){
                    BufferedReader in    = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintStream    out   = new PrintStream(socket.getOutputStream());
                    //send heart start info
                    out.println(CodeInfo.HEART_PACKAGE_START.getName());

                    String info = in.readLine();
                    if(info != null && info.equals(CodeInfo.HEART_PACKAGE_END.getName())){
                        t++; //add count times only when return info is right.
                    }
                    if(in != null){ //close input
                        in.close();
                    }
                    if(out != null){ //close output
                        out.close();
                    }
                    socket.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            long end = System.currentTimeMillis();
            connectTime[i] = end - start;
        }
        serverInfo.setOnline(t >= 2);   //if connect more than 2 times,then set online status true.
        serverInfo.setAverageDelayTime((connectTime[0] + connectTime[1] + connectTime[2]) / 3);     //set average time.
    }

    /**
     * send info to client
     * @param info      information to send.
     * @param client    client for send msg.
     */
    private void sendInfoToClient(String info,Socket client){
//        if(client == null)
    }

    public static void main(String[] args){
        final RegistryCenter center = new RegistryCenter();
        /** start a new thread to refresh server list */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        //refresh server list per 15 minutes
                        Thread.sleep(1000 * 900);
                        center.refreshServerList();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Command.RegistryCycle();
    }
}
