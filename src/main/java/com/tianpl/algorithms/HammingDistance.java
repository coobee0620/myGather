package com.tianpl.algorithms;

/**
 * Created by tianyu on 17/5/26.
 * Given two integers x and y, calculate the Hamming distance.
 * Hamming distance:
 * 两个码组之间,对应位置上码元异或结果之和
 * Note:
 * 0 ≤ x, y < 2^31.
 *
 * Example:
 * Input: x = 1, y = 4
 *
 * Output: 2
 *
 * Explanation:
 * 1   (0 0 0 1)
 * 4   (0 1 0 0)
 *        ↑   ↑
 * The above arrows point to positions where the corresponding bits are different.
 *
 */
public class HammingDistance {
    public static int calculate(int x,int y) {
        int d = 0;
        char[] xGroup = Integer.toBinaryString(x).toCharArray();
        char[] yGroup = Integer.toBinaryString(y).toCharArray();
        if (xGroup.length > yGroup.length) {
            yGroup = zeroize(yGroup,xGroup);
        } else if (xGroup.length < yGroup.length) {
            xGroup = zeroize(xGroup,yGroup);
        }
        int index = 0;
        while (index < xGroup.length || index < yGroup.length) {
            int xMeta = index < xGroup.length ? Integer.valueOf(String.valueOf(xGroup[index]))  : 0;
            int yMeta = index < yGroup.length ? Integer.valueOf(String.valueOf(yGroup[index]))  : 0;

            d += (xMeta ^ yMeta);
            index ++;
        }
        System.out.println(d);
        return d;
    }

    private static char[] zeroize(char[] target,char[] pole) {
        if (target == null || pole == null || pole.length <= target.length) {
            return target;
        }
        char[] result = new char[pole.length];
        for (int j = 0,i = result.length -1;i >=0 ;i--,j++) {
            if (j < target.length) {
                result[i] = target[target.length - 1 - j];
            } else {
                result[i] = '0';
            }
        }
        return result;
    }

    /**
     * 牛逼
     * @param x
     * @param y
     * @return
     */
    public static int calculate1Line(int x, int y) {
        return Integer.bitCount(x ^ y);
    }

    public static void main(String[] args) {
        System.out.println(calculate1Line(1,4));
    }
}
