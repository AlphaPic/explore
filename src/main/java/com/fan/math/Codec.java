package com.fan.math;

public class Codec {
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        return null;
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        return null;
    }

    public static TreeNode generateBinaryTree(int[] arr){
        if(arr == null || arr.length <= 0){
            return null;
        }
        if(arr.length == 1){
            return new TreeNode(arr[0]);
        }

        return null;
    }

    public static void printBinaryTree(TreeNode root){

    }
}
