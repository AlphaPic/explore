package com.fan.nio;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * @author:fanwenlong
 * @date:2018-01-18 09:51:55
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:套接字Channel的探索
 * @detail:
 */
public class SocketChannelExplore {

    public static void main(String[] args){
        try {
            Selector selector = Selector.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
