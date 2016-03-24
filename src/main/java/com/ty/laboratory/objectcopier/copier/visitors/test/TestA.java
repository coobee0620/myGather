package com.ty.laboratory.objectcopier.copier.visitors.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/12
 */
public class TestA {
    private String a;
    private String b;
    private String c;
    private int d;
    private long e;
    private String f;
    private String G1;
    private String I1;

    private int K1;

    private TestC tc;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getG1() {
        return G1;
    }

    public void setG1(String g1) {
        G1 = g1;
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String i1) {
        I1 = i1;
    }

    public int getK1() {
        return K1;
    }

    public void setK1(int k1) {
        K1 = k1;
    }

    public TestC getTc() {
        return tc;
    }

    public void setTc(TestC tc) {
        this.tc = tc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("a", a)
                .append("b", b)
                .append("c", c)
                .append("d", d)
                .append("e", e)
                .append("f", f)
                .append("G1", G1)
                .append("I1", I1)
                .append("K1", K1)
                .append("tc", tc)
                .toString();
    }
}
