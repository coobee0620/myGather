package com.tianpl.bb.common.lang;

import java.util.HashSet;
import java.util.Set;

/**
 * @project myGather
 * @description desc
 * @auth changtong.tianpl
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
        long num = (int)(1+Math.random()*(10000000000L));
        System.out.println(num);
        String b62 = Encoding(num);
        System.out.println(b62);
        System.out.println(Decoding(b62));
    }
}
