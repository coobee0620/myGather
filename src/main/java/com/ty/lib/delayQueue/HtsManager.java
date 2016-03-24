package com.ty.lib.delayQueue;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @project htq
 * @description desc
 * @auth changtong.ty
 * @date 2015/10/26
 */
public class HtsManager {
    private final static Log log = LogFactory.getLog(HtsManager.class);
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private DelayQueue<HtsDeratedTask> delayQueue = new DelayQueue<HtsDeratedTask>();

    @PostConstruct
    public void init() {
        es.submit(new DelayCosumer());
    }

    class DelayCosumer implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    delayQueue.take().call();
                } catch (InterruptedException e) {
                    log.error("DelayCosumer退出中断");
                    break;
                } catch (Throwable t) {
                    log.error("DelayCosumer发生未知异常", t);
                }
            }
            log.error("DelayCosumer线程结束");
        }
    }

//    public void registerDelayTask(Resumable resumable, Date excuteTime) {
//        long duration = excuteTime.getTime() - System.currentTimeMillis();
//        HtsDeratedTask htsDeratedTask = new HtsDeratedTask(resumable.bizUuid(),duration, TimeUnit.MILLISECONDS);
//        delayQueue.add(htsDeratedTask);
//    }
}
