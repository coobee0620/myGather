package com.tianpl.laboratory.zk.tester;

import java.util.PriorityQueue;

/**
 * Created by tianyu on 16/12/19.
 */
public class PriorityQ {
    public static void main(String[] args) {
        PriorityQueue<Integer> maxQ;
        maxQ = new PriorityQueue<>((o1, o2) -> o2 - o1);
        maxQ.add(-1);
        maxQ.add(2);
        maxQ.add(4);
        maxQ.add(-4);
        System.out.println(maxQ.poll());

        PriorityQueue<Integer> minQ;
        minQ = new PriorityQueue<>();
        minQ.add(-1);
        minQ.add(2);
        minQ.add(4);
        minQ.add(-4);
        System.out.println(minQ.poll());
    }
}
