package com.fan.utils;

import java.util.Random;

/**
 * @author:fanwenlong
 * @date:2018-05-08 14:14:29
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ProcessUtil {
    public static String getRandomProcessId(){
        //part 1
        long part1 = System.nanoTime();
        Random random = new Random(part1);
        //part 2
        long part2 = random.nextLong();
        //part 3
        long part3 = Thread.currentThread().getId();

        StringBuilder sb = new StringBuilder(100);
        sb.append(part1)
          .append(part2)
          .append(part3);
        return sb.toString();
    }
}
