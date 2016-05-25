package com.tianpl.laboratory.io.bio;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by tianyu on 5/20/16.
 */
public class Echo {
    public static void main(String[] args) throws Exception{
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s = stdin.readLine()) != null && s.length() != 0) {
            System.out.println(s);
        }
    }
}
