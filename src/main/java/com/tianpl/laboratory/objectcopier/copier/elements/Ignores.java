package com.tianpl.laboratory.objectcopier.copier.elements;

import com.tianpl.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.tianpl.laboratory.objectcopier.exception.CopyFormatException;
import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.List;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/12
 */
public class Ignores extends AbstractCopierDescrptor {
    private List<Field> fields;

    private Converter converter;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
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
