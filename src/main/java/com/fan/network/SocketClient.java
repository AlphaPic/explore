package com.fan.network;

import com.fan.utils.PatternUtils;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import static com.fan.network.Behavior.DOWNLOAD;
import static com.fan.network.Behavior.LOGIN;
import static com.fan.network.Behavior.UPLOAD;

/**
 * @author:fanwenlong
 * @date:2018-03-14 17:01:54
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class SocketClient {
    public static String SERVER_IP    = "127.0.0.1";
    public static Integer SERVER_PORT = 12345;

    private static String token;    //登录许可证
    private static String userName; //登录用户名

    static {
        token = null;
        userName = "null";
    }

    /**
     * 执行客户端指令
     */
    public void execute(){
        Socket client = null;
        try{
            boolean connectStat = true;
            while (connectStat) {
                System.out.print(userName + ">");
                BufferedReader preInput = new BufferedReader(new InputStreamReader(System.in));
                String preCmd = preInput.readLine();
                if(preCmd.startsWith("connect") == false){
                    System.out.println("unSupport Command '" + preCmd + "'.try it again");
                    continue;
                }
                userName = SocketClient.SERVER_IP;
                client = new Socket(SocketClient.SERVER_IP, SocketClient.SERVER_PORT);
                if (client == null) {
                    System.out.println("Server connected failed :(");
                    continue;
                }
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                PrintStream out = new PrintStream(client.getOutputStream());
                OutputStream outputStream = client.getOutputStream();

                InputStream clientInput = client.getInputStream();
                BufferedReader buf = new BufferedReader(new InputStreamReader(clientInput));
                boolean flag = true;
                System.out.println("Connected successful!!!\nEnter 'quit' for disconnected");
                System.out.println("----START----");
                while (flag) {
                    System.out.print(userName + ">");
                    String cmd = input.readLine();
                    //发送数据到服务端
                    if ("quit".equals(cmd) || "exit".equals(cmd)) {
                        flag = false;
                        connectStat = false;
                        out.println(cmd);
                    } else {
                        try {
                            /** 预处理指令 */
                            OperationVo operation = preHandleCmd(cmd);
                            if (operation == null) {
                                System.out.println("unSupport Command [" + cmd + "]");
                                continue;
                            }
                            cmd = operation.getHandledCmd();
                            if (cmd == null || cmd.isEmpty()) {
                                continue;
                            }
                            //往服务器中写入指令
                            out.println(cmd);
                            //等待服务器的回应
                            String response = buf.readLine();
                            if (response == null || response.isEmpty()) {
                                continue;
                            }

                            if (response.startsWith(CodeInfo.LOGIN_SUCCESS.getName())) {//在获准同意之后才进行下一步的措施
                                String[] info = response.trim().split(":");
                                if (info.length == 3) {
                                    token    = info[1];
                                    userName = info[2];
                                    System.out.println("Login successfully :)");
                                }
                            } else if (response.startsWith(CodeInfo.UPLOAD_STARTED.getName())) {//上载在允许登录开始
                                String[] cmds = PatternUtils.pattern.split(cmd.trim());
                                if (cmds.length <= 2) {
                                    continue;
                                }
                                File file = new File(cmds[2]);
                                System.out.println(file.length());
                                System.out.println(file.exists());
                                InputStream stream = new FileInputStream(file);
                                int len = 0;
                                byte[] msg = new byte[1024];
                                while ((len = stream.read(msg)) != -1) {
                                    outputStream.write(msg);
                                }
                                outputStream.write(-1);//作为结束符
                                stream.close();
                                String finish = buf.readLine();
                                if (finish.equals(CodeInfo.UPLOAD_FINISHED.getName())) {
                                    System.out.println("upload successfully :)");
                                }
                            } else if (response.startsWith(CodeInfo.DOWNLOAD_STARTED.getName())) {//下载在允许登录的时候开始
                                String fullPath = operation.getPath().endsWith("\\\\")
                                        ? operation.getPath() + operation.getFile()
                                        : operation.getPath() + "\\\\" + operation.getFile();
                                File file = new File(fullPath);
                                FileOutputStream fos = new FileOutputStream(file);
                                int len = 0;
                                byte[] msg = new byte[1024];
                                while ((len = clientInput.read(msg)) != 1) {
                                    fos.write(msg, 0, len);
                                }
                                fos.flush();
                                fos.close();
                                String finish = buf.readLine();// when it come to end,stay here for server response.
                                if (finish.equals(CodeInfo.DOWNLOAD_FINISHED.getName())) {
                                    System.out.println("download successfully :)");
                                }
                            } else {
                                System.out.println("error code " + response);
                            }
                        } catch (SocketTimeoutException e) {
                            System.out.println("Time out, No response");
                        }
                    }
                }
                System.out.println("Server closed!!!");
                System.out.println("----END----");
                input.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(client != null){
                //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
                try {
                    client.close(); //只关闭socket，其关联的输入输出流也会被关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 预处理指令
     * @param cmd
     */
    private OperationVo preHandleCmd(String cmd){
        if(cmd == null || cmd.isEmpty()){
            System.out.println("Input order can't be empty.");
            return null;
        }
        OperationVo vo = new OperationVo();
        String[] cmdArr = PatternUtils.pattern.split(cmd.trim());
        StringBuilder sb = new StringBuilder(100);
        Behavior behavior = Behavior.getBehavior(cmd);
        switch (behavior){
            case LOGIN:
                if (cmdArr.length != 3){
                    return null;
                }
                vo.setBehavior(LOGIN).setHandledCmd(cmd);
                return vo;
            //上载和下载代码必须在后面添加token
            case UPLOAD:
                if(cmdArr.length == 2){
                    if(loginCheck() == false){
                        System.out.println("have not login yet,please login first.");
                        return null;
                    }
                    String fileName = cmdArr[1];
                    File file = new File(fileName);
                    if(file.exists() == false){
                        System.out.println("file does't exists :(");
                        return null;
                    }
                    String afterCmd = sb.append(cmdArr[0])
                                        .append(" ")
                                        .append(token)  //设置令牌
                                        .append(" ")
                                        .append(cmdArr[1]).toString();
                    vo.setBehavior(UPLOAD)
                      .setFile(fileName)
                      .setHandledCmd(afterCmd);
                    return vo;
                }
                System.out.println("Illegal cmd '" + cmd + "' :(");
                return null;
            case DOWNLOAD:
                if(cmdArr.length == 3) {
                    if (loginCheck() == false) {
                        System.out.println("have not login yet,please login first.");
                        return null;
                    }
                    String fileName = cmdArr[1];    //文件
                    String pathName = cmdArr[2];    //目录

                    File path = new File(pathName);
                    if(path == null || path.exists() == false || path.isDirectory() == false){
                        System.out.println("directory doesn't exists or current file'" + path.getName() + "' is't a directory");
                        return null;
                    }
                    if(isFileInDir(path,fileName) == true){
                        return null;
                    }

                    String afterCmd = sb.append(cmdArr[0]).append(" ")
                                        .append(token).append(" ")
                                        .append(cmdArr[1]).toString(); //file name
                    vo.setFile(fileName).setBehavior(DOWNLOAD).setPath(pathName).setHandledCmd(afterCmd);
                    return vo;
                }
                System.out.println("Illegal cmd '" + cmd + "' :(");
                return null;
            case EXIT:

                return null;
            case HELP:
                return vo;
            case UNKNOWN:
            default:
                return null;
        }
    }

    /**
     * determine if a file is in specified directory.
     * @param path
     * @param file
     * @return true represent 'in',false represent 'not in'.
     */
    private boolean isFileInDir(File path,String file){
        if(file == null || file.isEmpty()){
            System.out.println("file name can't be empty.");
            return true;
        }
        String[] lists = path.list();
        if(lists.length == 0){
            return false;
        }
        for(String fileName : lists){
            if(fileName.equals(file)){
                System.out.println(file + "is already in directory.");
                return true;
            }
        }
        return false;
    }

    /**
     * check if admin or customer is login.
     * @return
     */
    private boolean loginCheck(){
        if (token == null || token.isEmpty()){
            return false;
        }
        return true;
    }

    public void print(String str){
        System.out.print(userName + ">" + str);
    }

    public void println(String str){
        print(userName + ">" + str + "\n");
    }

    public static void main(String[] args) throws Exception{
        new SocketClient().execute();
    }
}
