package com.tianpl.laboratory.typesafe.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by tianyu on 17/5/27.
 */
public class Tester {
    public static void main(String[] args) {
        Config config = ConfigFactory.load("aa.conf");
        Config config1 = ConfigFactory.load("bb.properties");
        Config finalConfig = config.withFallback(config1);
        System.out.println(finalConfig.getString("a.b"));
    }
}
