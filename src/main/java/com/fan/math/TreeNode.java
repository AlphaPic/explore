package com.fan.math;

public class TreeNode {
    private int val;
    private TreeNode leftChild;
    private TreeNode rightChild;

    public TreeNode(int val){
        this.val = val;
        leftChild = null;
        rightChild = null;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }
}
