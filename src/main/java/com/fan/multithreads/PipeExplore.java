package com.fan.multithreads;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * @author:fanwenlong
 * @date:2018-01-18 20:14:33
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:管道流的管理
 * @detail:
 */
public class PipeExplore {
    public static void main(String[] args){
        PipedReader in = new PipedReader();
        PipedWriter out = new PipedWriter();

        try {
            out.connect(in);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Thread printThread = new Thread(new PrintThread(in),"printThread");
        printThread.start();

        int receive = -1;
        try {
            while ((receive = System.in.read()) != -1){
                out.write(receive);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 打印线程 */
    static class PrintThread implements Runnable{
        PipedReader reader;

        public PrintThread(PipedReader reader){
            this.reader = reader;
        }

        @Override
        public void run() {
            try {
                int c = -1;
                while ((c = reader.read()) != -1){
                    System.out.print((char) c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
