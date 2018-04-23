package com.fan.math;

/**
 * @author:fanwenlong
 * @date:2018-04-23 17:53:51
 * @E-mail:alpha18603074401@gmail.com
 * @mobile:186-0307-4401
 * @description:
 * @detail:
 */
public class Hard {

    public ListNode mergeKLists(ListNode[] lists) {
        if(lists == null || lists.length <= 0)
            return null;
        ListNode head = null;
        ListNode tail = null;
        for(int i = 0;i < lists.length ; i++){
            if(lists[i] == null)
                continue;
            if(i == 0){
                head = lists[i];
                tail = head;
            }
            tail = endOfList(lists[i],tail);
        }

        return writeBack(quickSort(copyValFromList(head)));
    }

    private ListNode writeBack(int[] arr) {
    }

    private int[] quickSort(int[] arr) {
    }

    private int[] copyValFromList(ListNode head) {
    }

    private ListNode endOfList(ListNode list,ListNode tail) {
    }

    public static void main(String[] args){
        int SELECT = 3;
        Simple simple = new Simple();
        Hard demo = new Hard();
        switch (SELECT) {
            case 1:
                ListNode list1 = simple.buildListByArr(new int[]{1,3,5,7,8});
                ListNode list2 = simple.buildListByArr(new int[]{3,9,123,888});
                ListNode list3 = simple.buildListByArr(new int[]{2,54,100,123});
                ListNode list4 = simple.buildListByArr(new int[]{1,3,12});
                ListNode list5 = simple.buildListByArr(new int[]{3,19,23,98,124,156});
                ListNode head = demo.mergeKLists(new ListNode[]{list1,list5});
                StringBuilder sb1 = new StringBuilder();
                while (head.next != null){
                    sb1.append(head.val).append("->");
                    head = head.next;
                }
                sb1.append(head.val);
                System.out.println(sb1.toString());
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}
