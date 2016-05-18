package com.tianpl.laboratory.objectcopier.copier.visitors;


import com.tianpl.laboratory.objectcopier.copier.elements.*;
import com.tianpl.laboratory.objectcopier.exception.CopyFormatException;
import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.List;

/**
 * @project hrc
 * @description 访问者模式：抽象访问者
 * @auth changtong.tianpl
 * @date 2014/12/11
 */
public interface CopierVisitor {
    void visit(Copier descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(Converter descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(Ignores descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(CustomBuilt descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(Field descrptor, List<Field> availableField) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(Field descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(CompressorDecorator descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(MethodDecorator descrptor) throws CopyFormatException, CopyUnsuccessfullException;

    void visit(Decorator descrptor) throws CopyFormatException, CopyUnsuccessfullException;
}
