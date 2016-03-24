package com.ty.laboratory.objectcopier.copier.elements;


import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;

import java.util.List;

/**
 * @project hrc
 * @description ������ģʽ������Ԫ�ؽ�ɫ��Concrete Element��
 * @auth changtong.ty
 * @date 2014/12/10
 */
public class Decorator extends AbstractCopierDescrptor {
    private List<CompressorDecorator> compressorDecorators;

    private List<MethodDecorator> methodDecorators;

    private Converter converter;

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public List<CompressorDecorator> getCompressorDecorators() {
        return compressorDecorators;
    }

    public void setCompressorDecorators(List<CompressorDecorator> compressorDecorators) {
        this.compressorDecorators = compressorDecorators;
    }

    public List<MethodDecorator> getMethodDecorators() {
        return methodDecorators;
    }

    public void setMethodDecorators(List<MethodDecorator> methodDecorators) {
        this.methodDecorators = methodDecorators;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
