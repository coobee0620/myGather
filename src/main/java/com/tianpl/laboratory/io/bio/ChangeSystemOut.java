package com.tianpl.laboratory.io.bio;

import java.io.PrintWriter;

/**
 * Created by tianyu on 5/20/16.
 */
public class ChangeSystemOut {
    public static void main(String[] args) {
        try (PrintWriter pw = new PrintWriter(System.out)){
            pw.println("hello world");
        }
    }
}
