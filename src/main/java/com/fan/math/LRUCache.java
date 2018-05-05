package com.fan.math;

import java.util.HashMap;

public class LRUCache {
    private final int DEFAULT_CAPACITY = 10;
    private int capacity;
    private int total;
    private CacheNode[] nodeArr;

    class CacheNode{
        int key;
        int value;
        long curTime;
        CacheNode next;
        public CacheNode(int key,int value){
            this.key    = key;
            this.value  = value;
            curTime     = System.nanoTime();
            next        = null;
        }
    }
    /**
     * build a cache
     * @param capacity
     */
    public LRUCache(int capacity) {
        if(capacity <= 0){
            this.capacity = DEFAULT_CAPACITY;
        }else {
            this.capacity = capacity;
        }
        this.nodeArr = new CacheNode[this.capacity];
        this.total   = 0;
    }

    public int get(int key) {
        int index = key % capacity;
        if(nodeArr[index] == null){
            System.out.println("getVal:[" + key + ",-1]");
            return -1;
        }else {
            CacheNode tail = nodeArr[index];
            while (tail != null){
                if(tail.key == key){
                    System.out.println("getVal:[" + key + "," + tail.value + "]");
                    tail.curTime = System.nanoTime();
                    return tail.value;
                }
                tail = tail.next;
            }
            System.out.println("getVal:[" + key + ",-1]");
            return -1;
        }
    }

    public void put(int key, int value) {

        int len = nodeArr.length;
        int index = key % capacity;
        //if empty,add it.else if not empty go to end and append it.
        CacheNode node = new CacheNode(key,value);
        if(nodeArr[index] == null){
            if(total == capacity){
                removeLRUElem(nodeArr);
            }
            nodeArr[index] = node;
        }else {
            CacheNode tail = nodeArr[index];
            CacheNode before = tail;
            while (tail != null){
                int tailKey = tail.key;
                if(tailKey == node.key){
                    System.out.println("replaced:[" + tailKey + "," + tail.value + "] ---> [" + tailKey + "," + value + "]");
                    tail.value = value;
                    tail.curTime = System.nanoTime();
                    return;
                }
                before = tail;
                tail = tail.next;
            }
            before.next = node;
            if(total == capacity){
                removeLRUElem(nodeArr);
            }

        }
        if(total < capacity){
            total++;
        }
    }

    private void removeLRUElem(CacheNode[] nodeArr){
        int len = nodeArr.length;
        CacheNode del  = null;
        int headIndex = 0;
        int i = 0;
        for(;i < len;i++){
            if(nodeArr[i] == null){
                continue;
            }
            CacheNode scan = nodeArr[i];
            if(del == null){
                 del = scan;
                 headIndex = i;
            }
            while (scan != null){
                if(scan.curTime < del.curTime){
                    del = scan;
                    headIndex = i;
                }
                scan = scan.next;
            }
        }

        CacheNode head = nodeArr[headIndex];
        if(head == null || del == null){
            return;
        }
        if(head.key == del.key){
            nodeArr[headIndex] = head.next;
            head = null;
            return;
        }
        CacheNode pre  = head;
        CacheNode scan = head.next;
        while (scan != null){
            if(scan.key == del.key){
                pre.next = scan.next;
                scan = null;
                return;
            }
        }
    }

    public void printLRUCache(){
        int len = nodeArr.length;
        StringBuilder sb = new StringBuilder(100);
        for(int i = 0;i < len;i++){
            if(nodeArr[i] == null){
                sb.append("[ ]").append("\n");
                continue;
            }
            CacheNode node = nodeArr[i];
            while (node.next != null){
                sb.append("[").append(node.key).append(",").append(node.value).append("] -->");
            }
            sb.append("[").append(node.key).append(",").append(node.value).append("]\n");
        }
        System.out.println(sb.toString());
    }
}
