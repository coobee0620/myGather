package com.ty.laboratory.objectcopier.copier.elements;

import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.Map;

/**
 * @project hrc
 * @description 访问者模式：具体元素角色（Concrete Element）
 * @auth changtong.ty
 * @date 2014/12/9
 */
public class Copier extends AbstractCopierDescrptor {
    private String id;
    private Map<String, Converter> converters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Converter> getConverters() {
        return converters;
    }

    public void setConverters(Map<String, Converter> converters) {
        this.converters = converters;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
