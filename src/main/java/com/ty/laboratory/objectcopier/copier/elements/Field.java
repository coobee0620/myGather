package com.ty.laboratory.objectcopier.copier.elements;

import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;

/**
 * @project hrc
 * @description 访问者模式：具体元素角色（Concrete Element）
 * @auth changtong.ty
 * @date 2014/12/9
 */
public class Field extends AbstractCopierDescrptor {
    public enum ParentValidation {
        CUSTOMBUILT,
        IGNORES,
        COMPRESSOR
    }

    private String name;

    private String type;

    private String targetName;

    private String targetType;

    private ParentValidation parentValidation;

    private boolean diffType;

    private Converter converter;

    private CopierDescrptor parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean isDiffType() {
        return diffType;
    }

    public void setDiffType(boolean diffType) {
        this.diffType = diffType;
    }

    public ParentValidation getParentValidation() {
        return parentValidation;
    }

    public void setParentValidation(ParentValidation parentValidation) {
        this.parentValidation = parentValidation;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public CopierDescrptor getParent() {
        return parent;
    }

    public void setParent(CopierDescrptor parent) {
        this.parent = parent;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }


}
