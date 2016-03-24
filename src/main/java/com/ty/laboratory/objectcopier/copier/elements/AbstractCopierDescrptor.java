package com.ty.laboratory.objectcopier.copier.elements;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
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
