package com.ty.demo;

import java.util.concurrent.CountDownLatch;

/**
 * Created by tianyu on 16/4/26.
 */
public class CountDownLatchDemo implements Runnable {
    private int id;
    private CountDownLatch beginSignal;
    private CountDownLatch endSignal;

    public CountDownLatchDemo(int id, CountDownLatch begin, CountDownLatch end) {
        this.id = id;
        this.beginSignal = begin;
        this.endSignal = end;
    }

    @Override
    public void run() {
        try {
            System.out.println("预备..."+id);
            beginSignal.await();
            System.out.println("running..." +id);
            System.out.println("work" + id + "到达终点");
            endSignal.countDown();
            System.out.println("work" + id + "继续干其他事情");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CountDownLatch begSignal = new CountDownLatch(1);
        CountDownLatch endSignal = new CountDownLatch(8);

        try {
            for (int i = 0; i < 8; i++) {
                new Thread(new CountDownLatchDemo(i, begSignal, endSignal)).start();
            }
            begSignal.countDown();  //线程创建完毕,统一起跑

            endSignal.await();      //等待运动员到达终点
            System.out.println("结果发送到汇报成绩的系统");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
