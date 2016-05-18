package com.tianpl.lib.delayQueue;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @project htq
 * @description HTS出现异常造成任务注册失败时
 *              以此任务作为HTS失败的降级方案
 *              使用本机内存任务队列左右延迟调度的容器
 * @auth changtong.tianpl
 * @date 2015/10/26
 */
public class HtsDeratedTask implements Callable<Void>,Delayed {
    private String bizUuid;
    private long time; //nano

    public HtsDeratedTask(String bizUuid, long duration, @NotNull TimeUnit unit) {
        this.bizUuid = bizUuid;
        this.time = System.nanoTime() + TimeUnit.NANOSECONDS.convert(duration, unit);
    }

    public String getBizUuid() {
        return bizUuid;
    }

    public long getTime() {
        return time;
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        long result = this.getTime() - ((HtsDeratedTask) o).getTime();
        if (result > 0) {
            return 1;
        }
        if (result < 0) {
            return -1;
        }
        return 0;
    }
}
