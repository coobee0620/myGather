package com.ty.laboratory.objectcopier.copier.visitors.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/17
 */
public class TestC {
    private int cA;
    private int cB;

    public int getcA() {
        return cA;
    }

    public void setcA(int cA) {
        this.cA = cA;
    }

    public int getcB() {
        return cB;
    }

    public void setcB(int cB) {
        this.cB = cB;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cA", cA)
                .append("cB", cB)
                .toString();
    }
}
