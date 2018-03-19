package com.fan.utils;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * @author:fanwenlong
 * @date:2018-03-14 14:08:58
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class ArraysHandle {
    private Object[] objects;
    private int size = 0;
    private static final int CAPACITY = 4;

    public void stack(){
        objects = new Object[CAPACITY];
        System.out.println("创建了" + objects.length + "个单位容量");
    }

    public void push(Object e){
        ensureCapacity();
        objects[size++] = e;
    }

    public Object pop(){
        if(size > 0){
            return objects[--size];
        }
        throw new EmptyStackException();
    }

    public void ensureCapacity(){
        if(objects.length == size){
            objects = Arrays.copyOf(objects,size * 2 + 1);
            System.out.println("已扩容至" + objects.length);
        }
    }

    public static void main(String[] args){
        ArraysHandle handle = new ArraysHandle();
        handle.stack();

        for(int i = 0;i < 10;i++){
            Integer val = i;
            handle.push(val);
        }

        while (true){
            System.out.println(handle.pop());
        }
    }
}
