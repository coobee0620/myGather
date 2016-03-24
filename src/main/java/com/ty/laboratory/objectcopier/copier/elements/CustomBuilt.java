package com.ty.laboratory.objectcopier.copier.elements;

import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.List;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/12
 */
public class CustomBuilt extends AbstractCopierDescrptor {
    private Converter converter;
    private List<Field> fields;

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
