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
        int index = 0;
        ListNode head = lists[index];
        ListNode tail = null;

        while (head == null && index < lists.length - 1){
            index++;
            head = lists[index];
        }
        if(head == null)
            return null;
        tail = head;

        while (tail.next != null){
            tail = tail.next;
        }
        for(int i = index + 1;i < lists.length ; i++){
            if(lists[i] == null)
                continue;
            tail = endOfList(lists[i],tail);
        }

        return writeBack(quickSort(copyValFromList(head)),head);
    }

    private ListNode writeBack(int[] arr,ListNode head) {
        ListNode tail = head;
        int len = calcLengthOfList(tail);
        if(arr.length != len)
            return null;
        tail = head;
        int k = 0;
        while (tail != null){
            tail.val = arr[k++];
            tail = tail.next;
        }
        return head;
    }

    protected int[] quickSort(int[] arr) {
        if(arr == null || arr.length <= 0){
            return null;
        }
        basicSort(arr,0,arr.length - 1);
        return arr;
    }

    private void basicSort(int[] arr, int start, int end) {
        if(start < end) {
            int k = partition(arr,start,end);
            basicSort(arr,start,k - 1);
            basicSort(arr,k + 1,end);
        }
    }

    private int partition(int[] arr, int lo, int hi) {
        int key = arr[hi];
        int index = lo;
        int temp = 0;
        for (int i = lo;i <= hi;i++){
            if(arr[i] < key){
                temp = arr[i];
                arr[i] = arr[index];
                arr[index++] = temp;
            }
        }
        temp = arr[index];
        arr[hi] = temp;
        arr[index] = key;
        return index;
    }

    private int[] copyValFromList(ListNode head) {
        ListNode tail = head;
        int len = calcLengthOfList(tail);
        int[] arr = new int[len];
        tail = head;
        int k = 0;
        while (tail != null){
            arr[k++] = tail.val;
            tail = tail.next;
        }
        return arr;
    }

    private int calcLengthOfList(ListNode head) {
        if(head == null)
            return 0;
        int len = 0;
        while (head != null){
            len++;
            head = head.next;
        }
        return len;
    }

    private ListNode endOfList(ListNode list,ListNode tail) {
        if(tail == null)
            return null;
        while (tail.next != null){
            tail = tail.next;
        }

        if(list == null)
            return tail;
        tail.next = list;

        while (tail.next != null)
            tail = tail.next;
        return tail;
    }

    public static void main(String[] args){
        int SELECT = 1;
        Simple simple = new Simple();
        Hard demo = new Hard();
        switch (SELECT) {
            case 1:
//                ListNode list1 = simple.buildListByArr(new int[]{1,3,5,7,8});
//                ListNode list2 = simple.buildListByArr(new int[]{3,9,123,888});
//                ListNode list3 = simple.buildListByArr(new int[]{2,54,100,123});
//                ListNode list4 = simple.buildListByArr(new int[]{1,3,12});
//                ListNode list5 = simple.buildListByArr(new int[]{3,19,23,98,124,156});
                ListNode node = simple.buildListByArr(new int[]{});
                ListNode node1 = simple.buildListByArr(new int[]{1});
                ListNode head = demo.mergeKLists(new ListNode[]{node,node1});
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
