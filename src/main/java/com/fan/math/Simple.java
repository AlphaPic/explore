package com.fan.math;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author:fanwenlong
 * @date:2018-04-23 17:53:12
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class Simple {
    Integer ARRLEN = 80;

    /**
     * 计算数组中和为目标值的两个数
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        if(nums.length < 2)
            return null;
        else {
            int len = nums.length;
            for(int i = 0;i < len - 1;i++)
                for(int j = i + 1;j < len;j++){
                    if((nums[i] + nums[j]) == target){
                        return new int[]{i,j};
                    }
                }
        }
        return null;
    }

    public boolean hasCycle(ListNode head) {

        if(head == null || head.next == null || head.next.next == null){
            return false;
        }

        ListNode fast = head.next.next;
        ListNode slow = head.next;
        while (fast != null){
            if(slow == fast){
                return true;
            }
            if(fast.next != null){
                fast = fast.next.next;
            }
            slow = slow.next;
        }
        return false;
    }

    //给定两个非空链表来表示两个非负整数。位数按照逆序方式存储，它们的每个节点只存储单个数字。将两数相加返回一个新的链表
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if(l1 == null || l2 == null){
            return l1 == null ? (l2 == null ? null : l2) : l1;
        }
        int[] arr1 = buildArrByList(l1);
        int[] arr2 = buildArrByList(l2);

        if(arr1 == null || arr2 == null){
            return l1 == null ? (l2 == null ? null : l2) : l1;
        }

        int[] res = addTwoArrs(arr1,arr2);
        return buildListByArr(res);
    }

    private int getLenOfArr(int[] arr){
        if(arr == null || arr.length <= 0)
            return 0;
        int len = 0;
        for(int i = 0;i < arr.length;i++){
            if(arr[i] != -1)
                len++;
        }
        return len;
    }

    private int[] addTwoArrs(int[] arr1,int[] arr2){
        if(arr1 == null || getLenOfArr(arr1) <= 0)
            return arr2;
        if(arr2 == null || getLenOfArr(arr2) <= 0)
            return arr1;

        int len1 = getLenOfArr(arr1);
        int len2 = getLenOfArr(arr2);

        int[] min = null;
        int[] max = null;
        int minLen = 0;
        int maxLen = 0;
        if(len1 < len2){
            min = arr1;
            max = arr2;
            minLen = len1;
            maxLen = len2;
        }else {
            min = arr2;
            max = arr1;
            minLen = len2;
            maxLen = len1;
        }


        int[] res = new int[maxLen + 3];
        for(int i = 0;i < res.length;i++){
            res[i] = -1;
        }

        boolean ten = false;
        for(int i = 0;i < maxLen;i++){
            int val = 0;
            if(i < minLen) {
                val = min[i] + max[i] + (ten == true ? 1 : 0);
            } else{
                val = max[i] + (ten == true ? 1 : 0);
            }
            ten = (val / 10 > 0);
            res[i] = val % 10;
        }
        //补位
        if(ten == true){
            res[maxLen] = 1;
        }

        return res;
    }

    //把数组转化为整型数字
    private long turnArrToInteger(int[] arr){
        if(arr == null || arr.length <= 0)
            return 0;

        long sum = 0;
        for(int i = arr.length - 1;i >= 0;i--){
            if(arr[i] == -1) {
                continue;
            }
            sum = (sum * 10 + arr[i]);
        }
        return sum;
    }

    //根据数组建立一个链表
    protected ListNode buildListByArr(int[] arr){
        if(arr == null || arr.length <= 0){
            return null;
        }
        if(arr[0] == -1){
            return new ListNode(0);
        }
        int len = arr.length;
        ListNode head = new ListNode(arr[0]);
        head.next = null;
        ListNode tail = head;
        for(int i = 1;i < len;i++){
            if(arr[i] == -1)
                break;
            tail.next = new ListNode(arr[i]);
            tail = tail.next;
        }
        return head;
    }

    //把链表变成一个数字
    private int[] buildArrByList(ListNode list){
        if(list == null)
            return null;
        int[] arr = new int[ARRLEN];
        for(int i = 0;i < ARRLEN;i++){
            arr[i] = -1;
        }
        int j = 0;
        while (list != null){
            arr[j++] = list.val;
            list = list.next;
        }
        return arr;
    }

    public int reverse(int x) {
        boolean minor = false;
        if(x < 0) {
            minor = true;
            x = -x;
        }
        long val = 0;
        while (x > 0) {
            val = (val * 10 + x % 10);
            x = x / 10;
        }
        if(val < 0 || val > (long)(Integer.MAX_VALUE))
            return 0;
        return (int) (minor == true ? -val : val);
    }

    public ListNode deleteDuplicates(ListNode head) {
        if(head == null || head.next == null)
            return head;

        Set<Integer> set = new HashSet<>();
        ListNode before = head;
        ListNode after  = head.next;
        set.add(head.val);
        while (after != null){
            if(set.contains(after.val)){
                before.next = after.next;
                after = after.next;
            }else {
                set.add(after.val);
                before = after;
                after = after.next;
            }
        }

        return head;
    }

    public void printList(ListNode head){
        if(head == null)
            return;
        else {
            StringBuilder sb1 = new StringBuilder(100);
            while (head.next != null){
                sb1.append(head.val).append("->");
                head = head.next;
            }
            sb1.append(head.val);
            System.out.println(sb1.toString());
        }
    }

    public static void main(String[] args){
        int SELECT = 5;
        Simple demo = new Simple();
        switch (SELECT){
            case 1:
                int[] nums = {1,3,8,17};
                int target = 18;
                int[] res = demo.twoSum(nums,target);
                StringBuilder sb = new StringBuilder(30);
                if(res != null){
                    sb.append("[");
                    for(int i = 0;i < res.length - 1;i++){
                        sb.append(res[i]).append(",");
                    }
                    sb.append(res[res.length - 1]).append("]");
                    System.out.println(sb.toString());
                }else {
                    System.out.println("Not Found Yet.");
                }
                break;
            case 2:
                ListNode arr1 = demo.buildListByArr(new int[]{9});
                ListNode arr2 = demo.buildListByArr(new int[]{1,9,9,9,9,9,9,9,9,9});
                ListNode node = demo.addTwoNumbers(arr1,arr2);

                break;
            case 3:
                int NUMBER = 1534236469;
                System.out.println("The reverse of " + NUMBER + " is " + demo.reverse(NUMBER));
            case 4:
                ListNode list  = demo.buildListByArr(new int[]{});
                ListNode list1 = demo.buildListByArr(new int[]{1});
                ListNode head = list;
                ListNode tail = list;
//                while (tail.next != null){
//                    tail = tail.next;
//                }
//                tail.next = head;
                System.out.println("Has cycle?" + demo.hasCycle(head));
                break;
            case 5:
                ListNode list2 = demo.buildListByArr(new int[]{1,2,3,4,1,2,3,4,5,6});
                ListNode delete = demo.deleteDuplicates(list2);
                demo.printList(delete);
                break;
            default:
                break;
        }
    }
}
