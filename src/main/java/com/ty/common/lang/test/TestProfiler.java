package com.ty.common.lang.test;

import com.ty.common.lang.Profiler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * ≤‚ ‘œﬂ≥Ã¬Òµ„
 * Author: guangu.lj
 * Date:  002 6.2
 * Version 1.0.0
 */
public class TestProfiler {
    private static final Logger log = LoggerFactory.getLogger(TestProfiler.class);
    public void sleepSeconds(int seconds){
        Profiler.enter("sleepSeconds:" + seconds);
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error("sleep error", e);
        }finally {
            Profiler.release();
        }
    }

    public void sleepSecondsByTimes(int time){
        Profiler.enter("sleepSecondsByTimes:" + time);
        for(int i = 0; i< time; i++){
            sleepSeconds(1);
        }
        Profiler.release();
    }


    @Test
    public void testProfiler(){
        Profiler.start();
        sleepSeconds(2);
        sleepSecondsByTimes(3);
        Profiler.release();
        System.out.println(Profiler.dump());
        log.info(Profiler.dump());
    }
}
