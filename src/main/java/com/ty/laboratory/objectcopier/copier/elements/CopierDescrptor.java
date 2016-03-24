package com.ty.laboratory.objectcopier.copier.elements;


import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;

/**
 * @project hrc
 * @description 访问者模式的元素（Element）
 * @auth changtong.ty
 * @date 2014/12/10
 */
public interface CopierDescrptor {
    void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException;
}
