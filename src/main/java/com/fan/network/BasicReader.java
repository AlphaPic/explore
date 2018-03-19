package com.fan.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author:fanwenlong
 * @date:2018-03-14 15:46:13
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class BasicReader {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("www.csdn.com",80);
        Scanner in = new Scanner(socket.getInputStream());

        while (in.hasNextLine()){
            String line = in.nextLine();
            System.out.println(line);
        }
    }
}
