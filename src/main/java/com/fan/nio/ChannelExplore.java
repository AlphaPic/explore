package com.fan.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @author:fanwenlong
 * @date:2018-01-17 15:16:11
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:Nio的channel探索
 * @detail:
 */
public class ChannelExplore {
    private String Dir = "E:\\data\\git\\datasource\\";
    /**
     * 基本性质的探索
     */
    public void simpleExplore(){
        /** 通过通道来简单的获取文件的信息 */
        try {
            RandomAccessFile file = new RandomAccessFile(Dir + "a.txt","rw");
            FileChannel channel = file.getChannel();

            ByteBuffer buff = ByteBuffer.allocate(10);
            int bytesRead = channel.read(buff);
            while (bytesRead != -1){
                System.out.println("Read " + bytesRead);
                buff.flip();

                while (buff.hasRemaining()){
                    System.out.print((char)buff.get());
                }
                System.out.println();

                buff.clear();

                bytesRead = channel.read(buff);
            }
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从多个buffer往一个channel中写入数据,即gathering writing 操作
     */
    public void gatheringWriteTest(){
        try {
            RandomAccessFile file = new RandomAccessFile(Dir + "b.txt","rw");
            FileChannel channel = file.getChannel();

            ByteBuffer head  = ByteBuffer.allocate(128);
            ByteBuffer body  = ByteBuffer.allocate(1024);
            ByteBuffer[] buffers = {head,body};

            Long start = System.currentTimeMillis();
            int num = 1000000;
            int i = num;
            while(i-- > 0) {
                /** 清空buffer */
                head.clear();
                body.clear();

                /** 往buffer中写入数据 */
                StringBuilder header = new StringBuilder(128);
                header.append("name:alpha" + i + "\n");
                header.append("age:" + (18 + i) + "\n");
                header.append("sex:male\n");
                header.append("address:AnHui.District" + (6 + i) + ".\n");
                head.put(header.toString().getBytes());
                StringBuilder bodyInfo = new StringBuilder(1024);
                bodyInfo.append("Most young people have idols, and the popular stars are always treated in the first place in their hearts. In order to see their idols, some people will do a lot of crazy things, such as skip class or save money to buy a concert ticket. While in my heart, the best idols are our soldiers. They stay away homes and stand in the border to protect our safety. When we feel lucky to live in the peaceful time, we should think of these respectable guys. They sacrifice so much to protect our country. They are real idols and deserve our praise. When I see them in the TV, I admire them so much. Their images present people the sense of security. We see the power and courage from them, and they are the real idols.");
                body.put(bodyInfo.toString().getBytes());

                /** 切换buffer为读模式，将数据从其中读出来 */
                head.flip();
                body.flip();

                /** 写入 */
                channel.write(buffers);
            }
            file.close();
            Long end = System.currentTimeMillis();
            System.out.println("写入" + num + "个模板一共花去了" + (end - start) + "ms");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试在channel之间进行传输数据
     */
    public void channelTransfer(Boolean fromOrTo){
        try {
            RandomAccessFile from = new RandomAccessFile(Dir + "b.txt","rw");
            RandomAccessFile to   = new RandomAccessFile(Dir + "c.txt","rw");

            FileChannel fromChannel = from.getChannel();
            FileChannel toChannel   = to.getChannel();

            if(fromOrTo){
                toChannel.transferFrom(fromChannel,0,fromChannel.size());
            }else{
                fromChannel.transferTo(0,fromChannel.size(),toChannel);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ChannelExplore channel = new ChannelExplore();
        channel.channelTransfer(false);
    }
}
