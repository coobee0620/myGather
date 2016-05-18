package com.tianpl.laboratory;

import java.lang.reflect.Method;

/**
 * @project myGather
 * @description desc
 * @auth changtong.tianpl
 * @date 2015/11/2
 */
public class ReflexTest {
    public Void typeTest() {
        return null;
    }

    public static void main(String[] args) {
        Method[] methods = ReflexTest.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
            Class retType = method.getReturnType();
            System.out.println(retType.getName());
        }
    }
}
