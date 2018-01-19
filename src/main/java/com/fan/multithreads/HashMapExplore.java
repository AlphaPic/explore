package com.fan.multithreads;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author:fanwenlong
 * @date:2018-01-16 15:31:04
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:hashmap的探索
 * @detail:
 */
public class HashMapExplore {
    private Map<HashKeyObj,String> map;

    class HashKeyObj{
        String key;

        public HashKeyObj(String key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            if(key.equals("odd")){
                return 1;
            }else if(key.equals("even")){
                return 2;
            }else {
                return 3;
            }
        }
    }

    /**
     * 基本性质的探索
     */
    public void testSimpleFeature(){
        int number = 1000;
        map = new HashMap<HashKeyObj, String>(4);
        for(int i = 0;i < number;i++) {
            HashKeyObj key = new HashKeyObj(i % 2 == 0 ? "odd" : "even");
            String value = String.valueOf(i);
            String var = map.put(key,value);
            System.out.println(var);
        }
    }

    /***
     * hashmap和linkedhashmap的写入和读取速度比较
     */
    public void hashMapCompare(){
        Map<String,String> hashMap = new HashMap<String, String>();
        Map<String,String> linkedHashMap = new LinkedHashMap<String, String>();
        int number = 10000000;

        Long start = System.currentTimeMillis();
        for(int i = 0;i < number;i++){
            String key = "hashmap" + i;
            String val = "value";
            hashMap.put(key,val);
        }
        Long end1 = System.currentTimeMillis();
        System.out.println("写入" + number + "个数，hashMap一共花费" + (end1 - start) + "毫秒!");
        for(int i = 0;i < number;i++){
            String key = "hashmap" + i;
            String val = "value";
            linkedHashMap.put(key,val);
        }
        Long end2 = System.currentTimeMillis();
        System.out.println("写入" + number + "个数，linkedHashMap一共花费" + (end2 - end1) + "毫秒!");

        for(int i = 0;i < number;i++){
            String key = "hashmap" + i;
            hashMap.get(key);
        }
        Long end3 = System.currentTimeMillis();
        System.out.println("读出" + number + "个数，hashMap一共花费" + (end3 - end2) + "毫秒!");

        for(int i = 0;i < number;i++){
            String key = "hashmap" + i;
            linkedHashMap.get(key);
        }
        Long end4 = System.currentTimeMillis();
        System.out.println("读出" + number + "个数，linkedHashMap一共花费" + (end4 - end3) + "毫秒!");
    }

    /**
     * linkedHashMap的拿值探索
     */
    public void linkHashMapGetExplore(){
        Map<String,String> map = new LinkedHashMap<String, String>(100,0.75f,true);
        for(int i = 0;i < 100;i++){
            String key = "key" + i;
            String val = "val" + i;
            map.put(key,val);
        }
        for(int i = 0;i < 100;i++){
            String key = "key" + i;
            System.out.println(map.get(key));
        }
    }



    public static void main(String[] args){
        HashMapExplore explore = new HashMapExplore();
        explore.linkHashMapGetExplore();
    }
}
