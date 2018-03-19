package com.fan.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author:fanwenlong
 * @date:2018-03-19 14:37:38
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class Command {

    /**
     * 注册中心的循环
     */
    public static void RegistryCycle(){
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                String command = input.readLine();
//                if(command)
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
