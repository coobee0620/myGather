package com.ty.laboratory.objectcopier.copier.elements;

import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;

/**
 * @project hrc
 * @description 访问者模式：具体元素角色（Concrete Element）
 * @auth changtong.ty
 * @date 2014/12/10
 */
public class MethodDecorator extends AbstractCopierDescrptor {
    private String type;

    private String method;

    private Converter converter;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
