package com.tianpl.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by tianyu on 16/4/26.
 */
public class CyclicBarrierDemo implements Runnable{
    private CyclicBarrier cyclicBarrier;
    private int id;

    public CyclicBarrierDemo(int id, CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        this.id = id;
    }
    @Override
    public void run() {
        try {
            System.out.println("玩家" + id + "正在玩第一关...");
            cyclicBarrier.await();
            System.out.println("玩家" + id + "进入第二关...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, () -> System.out.println("所有玩家完成第一关！"));

        for (int i = 0; i < 4; i++) {
            new Thread(new CyclicBarrierDemo(i, cyclicBarrier)).start();
        }
    }
}
