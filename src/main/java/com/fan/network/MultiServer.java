package com.fan.network;


import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:fanwenlong
 * @date:2018-03-14 16:49:49
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class MultiServer {
    static class UserInfo{
        String name;
        String pass;
        String token;
        Boolean isLogin;

        public UserInfo(){}

        public UserInfo setName(String name){
            this.name = name;
            return this;
        }

        public UserInfo setPass(String pass){
            this.pass = pass;
            return this;
        }

        public UserInfo setToken(String token){
            this.token = token;
            return this;
        }

        public UserInfo setIsLogin(Boolean isLogin){
            this.isLogin = isLogin;
            return this;
        }

        public boolean isLogin(String tokenString){
            if(tokenString == null || tokenString.isEmpty()){
                return false;
            }
            return tokenString.equals(token) && isLogin;
        }
    }
    private static Integer SERVER_PORT = 12345;     //服务器端口
    private static Integer clientCount = 0;         //客户端数目
    private static ServerSocket server = null;      //服务端
    private static List<UserInfo> infoList = null;  //用户信息列表
    public static Pattern pattern = null;          //模式
    private static String fileDir = "E:\\data\\fileServer\\"; //文件路径

    /**
     * 初始化服务线程
     */
    static {
        try {
            server = new ServerSocket(SERVER_PORT);
            infoList = getInfoList();
            pattern = Pattern.compile("[ ]+");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户信息列表
     * @return
     */
    private static List<UserInfo> getInfoList(){
        List<UserInfo> infos = new ArrayList<UserInfo>();
        UserInfo info = new UserInfo();
        infos.add(info.setName("alpha")
                      .setPass("111111")
                      .setToken("222").setIsLogin(false));
        return infos;
    }
    /**
     * 执行主流程
     */
    public void execute(){
        int num = 0;

        /**
         * 创建服务端
         */
        while (server == null && num < 3){
            try {
                server = new ServerSocket(12345);
            } catch (IOException e) {
                e.printStackTrace();
            }
            num++;
        }

        if(server == null || num >= 3){
            System.out.println("can't create server...");
            return;
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,20,1000, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
        while (true){
            try {
                System.out.println("Listening...");
                Socket cli = server.accept();
                if(cli == null){
                    System.out.println("client connect failed :(");
                    continue;
                }
                String cliName = "client-" + getClientNameSuffix(clientCount);
                increaseClientNum(1);
                /** 创建一个客户端 */
                ThreadEchoHandler threadEchoHandler = new ThreadEchoHandler(cli,cliName);
                executor.execute(threadEchoHandler);
                System.out.println("client[" + cliName + "]added :)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getClientNameSuffix(Integer count){
        long ms = System.currentTimeMillis();
        String name = ms + "-" +count;
        return name;
    }

    /**
     * 查看当前客户端的数目
     */
    private void showCurrentClientNumber(){
        synchronized (clientCount){
            System.out.println("当前一共有" + clientCount + "个客户端");
        }
    }

    /**
     * 增加客户端数目
     * @param count
     */
    private void increaseClientNum(int count){
        synchronized (clientCount){
            clientCount += count;
        }
    }

    /**
     * 减少客户端数目
     * @param count
     */
    private static void decreaseClientNum(int count){
        synchronized (clientCount){
            if(clientCount > count){
                clientCount -= count;
            }else {
                clientCount = 0;
            }
        }
    }

    /**
     * 服务端线程
     */
    static class ThreadEchoHandler implements Runnable{
        private Socket client;
        private String clientName;

        PrintStream    out;     //输出流
        BufferedReader buf;     //输入流
        InputStream inputStream;
        OutputStream outputStream;

        private Integer retryTimes; //重试次数

        public ThreadEchoHandler(Socket client,String clientName){
            this.client     = client;
            this.clientName = clientName;
            this.retryTimes = 5;
        }

        @Override
        public void run() {
            try{
                outputStream = client.getOutputStream();
                out = new PrintStream(outputStream);
                inputStream = client.getInputStream();
                buf = new BufferedReader(new InputStreamReader(inputStream));

                boolean flag = true;
                while(flag){
                    //receive data from client
                    String str =  buf.readLine();
                    if(str == null || "".equals(str)){
                        out.println("Err:can't be empty");
                    }else{
                        if("quit".equals(str.trim().toLowerCase())){
                            flag = false;
                        }else{
                            /** handle basic command. */
                            handleInput(str);
                        }
                    }
                }
                System.out.println("client " + clientName + " has been closed,Adios!!!");
                decreaseClientNum(1);
                out.close();        //close output
                buf.close();        //close input
            }catch (Exception e){
                System.out.println("client " + clientName + " has been interrupted.");
            }finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * handle detail input
         * @param str
         */
        private void handleInput(String str) throws Exception{
            if(str == null || "".equals(str)){
                return;
            }
            str = str.trim();
            OperationVo operation = OperationVo.getOperationVo(str);

            Behavior behavior = Behavior.getBehavior(str);
            switch (behavior){
                case LOGIN:
                    String[] loginArr = split(str);
                    if(loginArr.length != 3){
                        out.println("Invalid login Command...");
                        return;
                    }else {
                        if(infoList == null || infoList.isEmpty()){
                            System.out.println("user info is empty");
                            out.println("fail to load userInfo :(");
                            return;
                        }
                        String user = loginArr[1];  //username
                        String pass = loginArr[2];  //password
                        Iterator iterator = infoList.iterator();
                        while (iterator.hasNext()){
                            UserInfo info = (UserInfo) iterator.next();
                            if(info.isLogin == true || (user.equals(info.name) && pass.equals(info.pass))){
                                info.setIsLogin(true);  //set login time
                                out.println(CodeInfo.LOGIN_SUCCESS.getName() + ":" + info.token.trim() + ":" + info.name.trim()); //write back to client.
                                return;
                            }
                        }
                        out.println("wrong 'username' or 'password' :(");
                    }
                    break;
                case UPLOAD:
                    String[] infoArr = split(str);
                    if(infoArr.length != 3){
                        out.println("upload cmd should like this '[upload token filepath]'");
                        return;
                    }
                    //if user is login.
                    boolean login = isTokenEffient(infoArr[1]);

                    String fileName = infoArr[2];
                    if(fileName == null || fileName.isEmpty()) {
                        login = false;
                    }
                    //write file
                    if(login){
                        String[] filesInfo = fileName.split("\\\\");
                        if(filesInfo.length <= 0){
                            out.println(CodeInfo.ERROR_CODE.getName());
                            return;
                        }
                        //send back transfer code and set the status read
                        out.println(CodeInfo.UPLOAD_STARTED.getName());
                        File file = new File(fileDir + filesInfo[filesInfo.length - 1]);
                        FileOutputStream fos = new FileOutputStream(file);
                        int a = 0;
                        byte[] writeArr = new byte[1024];
                        while ((a =  inputStream.read(writeArr)) != 1) {
                            fos.write(writeArr,0,a);
                        }
                        System.out.println("upload finished :)");
                        fos.flush();
                        //close file handle
                        fos.close();
                        out.println(CodeInfo.UPLOAD_FINISHED.getName());
                        return;
                    }
                    out.println("file could't be loaded :(");
                    break;
                case DOWNLOAD:
                    String token = operation.getToken();
                    if(isTokenEffient(token) == false){
                        System.out.println("invalid visit.");
                        out.println(CodeInfo.NOT_LOGIN_YET.getName());
                        return;
                    }

                    //judge if file exists.
                    String fileName1 = fileDir + operation.getFile();
                    File file = new File(fileName1);
                    if(file.exists() == false){
                        out.println(CodeInfo.FILE_NOT_FOUND.getName());
                        System.out.println("file not found.");
                        return;
                    }

                    out.println(CodeInfo.DOWNLOAD_STARTED.getName());//send success code back
                    System.out.println("transferring file [ " + fileName1 + " ] to client:" + clientName);
                    //transfer file
                    InputStream fis = new FileInputStream(file);
                    int len = 0;
                    byte[] msg = new byte[1024];
                    while ((len = fis.read(msg)) != -1){
                        outputStream.write(msg,0,len);
                    }
                    outputStream.write(-1); //end flag
                    fis.close();
                    out.println(CodeInfo.DOWNLOAD_FINISHED.getName());
                    break;
                case UNKNOWN:
                    System.out.println("unSupport command.");
                    break;
                default:
                    break;
            }
        }

        /**
         * 对字符串进行分离
         * @param str
         * @return
         */
        private String[] split(String str){
            if(str == null || str.isEmpty()){
                return null;
            }

            String[] arr = pattern.split(str);
            return arr;
        }

        /**
         * 判断token是不是有效
         * @param token
         * @return
         */
        private boolean isTokenEffient(String token){
            if(infoList == null || infoList.isEmpty()){
                return false;
            }
            Iterator iterator = infoList.iterator();
            while (iterator.hasNext()){
                UserInfo info = (UserInfo) iterator.next();
                if(info.isLogin(token)){
                    return true;
                }
            }
            return false;
        }


        /**
         * 发送给客户端信息
         * @param info
         */
        private void sendClientInfo(String info){

        }
    }

    public static void main(String[] args) throws Exception{
        new MultiServer().execute();
    }
}
