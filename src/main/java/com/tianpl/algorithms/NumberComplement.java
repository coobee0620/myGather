package com.tianpl.algorithms;

/**
 * Created by tianyu on 17/5/26.
 */
public class NumberComplement {
    public static int findComplement(int num) {
        return ~num & (Integer.highestOneBit(num) - 1);
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE*3);
    }
}
