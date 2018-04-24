package com.fan.math;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:fanwenlong
 * @date:2018-04-23 17:53:42
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class Common {
    //给定一个字符串，找出不含有重复字符的最长子串的长度
    public int lengthOfLongestSubstring(String s) {
        if(s == null || s.isEmpty()){
            return 0;
        }
        int maxLen = 0;
        int curLen = 0;
        char[] arr = new char[1000];
        clearSubArr(arr);
        int start = 0;
        for(int i = 0;i < s.length();){
            if(isInArr(arr,s.charAt(i))){
                start = start + findPos(arr,s.charAt(i)) + 1;
                i = start;
                clearSubArr(arr);
                curLen = 0;
            }else {
                arr[curLen++] = s.charAt(i);
                maxLen = maxLen > curLen ? maxLen : curLen;
                i++;
            }
        }
        return maxLen;
    }

    private int findPos(char[] arr, char c) {
        if(arr == null || arr.length <= 0)
            return 0;
        for(int i = 0;i < arr.length;i++){
            if(c == arr[i])
                return i;
        }
        return 0;
    }


    private void clearSubArr(char[] arr) {
        if(arr == null || arr.length == 0)
            return;
        for(int i = 0;i < arr.length;i++){
            arr[i] = '\0';
        }
    }

    private boolean isInArr(char[] arr, char c) {
        if(arr == null || arr.length <= 0){
            return false;
        }

        for(int i = 0;i < arr.length;i++){
            if(c == arr[i])
                return true;
        }
        return false;
    }

    public ListNode deleteDuplicates(ListNode head) {
        if(head == null || head.next == null){
            return head;
        }
        Map<Integer,Integer> map = new HashMap<>(10);
        ListNode tail = head;
        while (tail != null){
            int val = tail.val;
            if(map.get(val) != null){
                int temp = map.get(val);
                temp++;
                map.put(val,temp);
            }else {
                map.put(val,1);
            }
            tail = tail.next;
        }

        //execute delete action
        while (head != null && map.get(head.val) > 1){
            head = head.next;
        }
        if(head == null)
            return null;
        tail = head.next;
        ListNode temp = head;
        while (tail != null){
            int val = tail.val;
            if(map.get(val) == null || map.get(val) > 1){
                temp.next = tail.next;
            }else {
                temp = tail;
            }

            tail = tail.next;
        }

        return head;
    }


    public static void main(String[] args){
        int SELECT = 2;
        Common demo = new Common();
        Simple simple = new Simple();
        switch (SELECT){
            case 1:
                long start = System.currentTimeMillis();
                String s ="";
                System.out.println("The length of '" + s + "' is " + demo.lengthOfLongestSubstring(s) + "\ncost " + (System.currentTimeMillis() - start) + "ms");
                break;
            case 2:
                ListNode list = simple.buildListByArr(new int[]{1,1,1,2,3,4,5,6,7,8,3,4});
                ListNode head = demo.deleteDuplicates(list);
                simple.printList(head);
                break;
            default:
                break;
        }
    }
}
