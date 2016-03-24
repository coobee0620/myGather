package com.ty.alibaba.common.lang;

import java.util.HashSet;
import java.util.Set;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/9/15
 */
public class Base62 {
    private static String Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    public static String Encoding(long num) throws Exception {
        if (num < 1)
            throw new Exception("num must be greater than 0.");

        StringBuilder sb = new StringBuilder();
        for (; num > 0; num /= 62)
        {
            sb.append(Alphabet.charAt((int)(num % 62)));
        }
        return sb.toString();
    }

    public static long Decoding(String str) throws Exception {
        char[] strArray = str.trim().toCharArray();
        if (strArray.length < 1)
            throw new Exception("str must not be empty.");

        long result = 0;
        for (int i = 0; i < str.length(); i++)
        {
            long num = (long) (Alphabet.indexOf(strArray[i]) * Math.pow(62, i));
            result += num;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Set<String> set = new HashSet<String>();
        char[] chars = "1q2w3e4r5t1q2w3e4r5t1q2w3e4r5t1q2w3e4r5t1q2w3e4r5t1q2w3e4r5t1q2w3e4r5t1".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c:chars) {
            stringBuilder.append(Encoding(c));
        }
        System.out.println(stringBuilder.toString());
//        for (long i = 1;i <71 ;i++) {
//            String aNew = Encoding(i);
//            System.out.println(aNew);
//            if (set.contains(aNew)) {
//                System.out.println("SB");
//            } else {
//                set.add(Encoding(i));
//            }
//        }
    }
}
