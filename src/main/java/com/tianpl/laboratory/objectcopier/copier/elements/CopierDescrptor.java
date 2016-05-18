package com.tianpl.laboratory.objectcopier.copier.elements;


import com.tianpl.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.tianpl.laboratory.objectcopier.exception.CopyFormatException;
import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;

/**
 * @project hrc
 * @description 访问者模式的元素（Element）
 * @auth changtong.tianpl
 * @date 2014/12/10
 */
public interface CopierDescrptor {
    void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException;
}
