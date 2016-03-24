package com.ty.laboratory.objectcopier.copier.visitors.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/12
 */
public class Testb {
    private int a;
    private String b;
    private String c;
    private Integer d;
    private long e;
    private String h1;
    private String j1;

    private long l1;

    private Testd td;

    private String targetString;

    public int getA() {
        return a;
    }

    public void setA(int a) {
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

    public Integer getD() {
        return d;
    }

    public void setD(Integer d) {
        this.d = d;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getJ1() {
        return j1;
    }

    public void setJ1(String j1) {
        this.j1 = j1;
    }

    public long getL1() {
        return l1;
    }

    public void setL1(long l1) {
        this.l1 = l1;
    }

    public Testd getTd() {
        return td;
    }

    public void setTd(Testd td) {
        this.td = td;
    }

    public String getTargetString() {
        return targetString;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("a", a)
                .append("b", b)
                .append("c", c)
                .append("d", d)
                .append("e", e)
                .append("h1", h1)
                .append("j1", j1)
                .append("l1", l1)
                .append("td", td)
                .append("targetString", targetString)
                .toString();
    }
}
