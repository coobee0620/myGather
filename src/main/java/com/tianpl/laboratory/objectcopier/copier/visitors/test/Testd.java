package com.tianpl.laboratory.objectcopier.copier.visitors.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/17
 */
public class Testd {
    private int dA;
    private String dB;

    public int getdA() {
        return dA;
    }

    public void setdA(int dA) {
        this.dA = dA;
    }

    public String getdB() {
        return dB;
    }

    public void setdB(String dB) {
        this.dB = dB;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("dA", dA)
                .append("dB", dB)
                .toString();
    }
}
