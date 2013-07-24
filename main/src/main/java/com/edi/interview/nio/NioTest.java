package com.edi.interview.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class NioTest {

    public static void main(String[] args) {
        try {
            FileInputStream fin = new FileInputStream("c:/opt/test.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
