package com.edi.interview.btree;

public class BinaryTree {

    public BinaryTree createTree()
    {
        
        
        return null;
    }
    
    public static void main(String[] args) {
        int [] array = {1,3,5,7,4,9,21,2};
        
        
    }
    
    private class Node
    {
        Node left;
        Node right;
        int value;
        
        public Node getLeft() {
            return left;
        }
        public void setLeft(Node left) {
            this.left = left;
        }
        public Node getRight() {
            return right;
        }
        public void setRight(Node right) {
            this.right = right;
        }
        public int getValue() {
            return value;
        }
        public void setValue(int value) {
            this.value = value;
        }
        
    }
}
