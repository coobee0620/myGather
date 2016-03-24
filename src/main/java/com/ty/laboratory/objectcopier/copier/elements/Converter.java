package com.ty.laboratory.objectcopier.copier.elements;


import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;

/**
 * @project hrc
 * @description 对象间转换类。访问者模式：具体元素角色（Concrete Element）
 * @auth changtong.ty
 * @date 2014/12/9
 */
public class Converter extends AbstractCopierDescrptor {
    private String id;

    private Class from;

    private Class to;

    private CustomBuilt customBuilt;

    private Ignores ignores;

    private Decorator decorator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class getFrom() {
        return from;
    }

    public void setFrom(Class from) {
        this.from = from;
    }

    public Class getTo() {
        return to;
    }

    public void setTo(Class to) {
        this.to = to;
    }

    public CustomBuilt getCustomBuilt() {
        return customBuilt;
    }

    public void setCustomBuilt(CustomBuilt customBuilt) {
        this.customBuilt = customBuilt;
    }

    public Ignores getIgnores() {
        return ignores;
    }

    public void setIgnores(Ignores ignores) {
        this.ignores = ignores;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
