package com.fan.autowiki;


import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:fanwenlong
 * @date:2018-06-11 09:43:56
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class CommandHandle {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 执行命令行操作
     * @return
     */
    public String executeCmd(String cmd){
        String err;
        String str;
        String charset = Charset.defaultCharset().toString();
        InputStream inputStream = null;
        InputStream errorStream = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            inputStream   = process.getInputStream();
            errorStream   = process.getErrorStream();
            str = processStdout(inputStream,charset);
            err = processStdout(errorStream,charset);
            if(err != null && !err.isEmpty()){
                return str + err;
            }

        }catch (Exception e){
            err = e.getMessage();
            return err;
        }finally {
            closeInputStream(inputStream,errorStream);
        }
        return str;
    }

    /**
     * 关闭输入流
     * @param inputStreams
     * @return
     */
    private void closeInputStream(InputStream...inputStreams){
        try {
            for(InputStream inputStream : inputStreams) {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理标准输出
     * @param io
     * @param charset
     * @return
     */
    private String processStdout(InputStream io, String charset) throws IOException {
        BufferedReader buf = new BufferedReader(new InputStreamReader(io));
        StringBuilder sb = new StringBuilder(100);
        String s;
        try{
            while ((s = buf.readLine()) != null){
                sb.append("\n").append(s);
            }
        } finally {
            buf.close();
        }

        return sb.toString();
    }

    public static void main(String[] args){
        CommandHandle transfer = new CommandHandle();

        System.out.println(transfer.executeCmd("git"));
    }
}
