package com.tianpl.laboratory.objectcopier.copier.elements;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/11
 */
public abstract class AbstractCopierDescrptor implements CopierDescrptor {
    protected boolean available = false;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
