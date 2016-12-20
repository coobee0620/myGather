package com.tianpl.laboratory.algorithm;

import org.springframework.util.Assert;

/**
 * Created by tianyu on 16/12/19.
 */
public class MaxHeap {
    public static void maxHeapify(int[] a, int i)
    {
        int l = left(i);
        int r = right(i);
        int largest = i;

        if(l < a.length && a[l] > a[i])
            largest = l;
        if(r < a.length && a[r] > a[largest])
            largest = r;
        if(i != largest)
        {
            swap(a, i, largest);
            maxHeapify(a, largest);
        }
    }

    private static int left(int i)
    {
        return i * 2 + 1;
    }

    private static int right(int i)
    {
        return i * 2 + 2;
    }

    private static void swap(int[] a, int i, int largest) {
        Assert.isTrue(a.length >= i +1);
        Assert.isTrue(a.length >= largest +1);
        int tmp = a[i];
        a[i] = a[largest];
        a[largest] = tmp;
    }

    public static void main(String[] args) {
        int[] arr = {-1,-6,3,8,11,192,56,-54};
        MaxHeap.maxHeapify(arr,0);
        for (int a : arr) {
            System.out.println(a);
        }
        System.out.println("_____");
        MaxHeap.maxHeapify(arr,0);

        for (int a : arr) {
            System.out.println(a);
        }
    }
}
