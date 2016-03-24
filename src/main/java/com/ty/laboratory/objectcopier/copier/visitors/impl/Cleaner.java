package com.ty.laboratory.objectcopier.copier.visitors.impl;


import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.elements.*;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;

import java.util.List;
import java.util.Map;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/12
 */
public final class Cleaner implements CopierVisitor {
    @Override
    public void visit(Copier descrptor) throws CopyFormatException {
        if (descrptor != null && descrptor.getConverters() != null && descrptor.getConverters().size() > 0) {
            Map<String, Converter> converterMap = descrptor.getConverters();
            for (Map.Entry<String, Converter> converterEntry : converterMap.entrySet()) {
                if (!converterEntry.getValue().isAvailable()) {
                    converterMap.remove(converterEntry.getKey());
                } else {
                    visit(converterEntry.getValue());
                }
            }
        }
    }

    @Override
    public void visit(Converter descrptor) throws CopyFormatException {
        if (descrptor != null) {
            if (descrptor.getCustomBuilt() != null) {
                CustomBuilt customBuilt = descrptor.getCustomBuilt();
                if (!customBuilt.isAvailable()) {
                    descrptor.setCustomBuilt(null);
                } else {
                    visit(customBuilt);
                }
            }

            if (descrptor.getIgnores() != null) {
                Ignores ignores = descrptor.getIgnores();
                if (!ignores.isAvailable()) {
                    descrptor.setIgnores(null);
                } else {
                    visit(ignores);
                }
            }

            if (descrptor.getDecorator() != null) {
                Decorator decorator = descrptor.getDecorator();
                if (!decorator.isAvailable()) {
                    descrptor.setDecorator(null);
                } else {
                    visit(decorator);
                }
            }
        }
    }

    @Override
    public void visit(Ignores descrptor) throws CopyFormatException {
        if (descrptor != null) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (int i = 0; i < fields.size(); i++) {
                    if (!fields.get(i).isAvailable()) {
                        fields.remove(i);
                    }
                }
            }
            if (fields == null || fields.size() == 0) {
                descrptor.setAvailable(false);
            }
        }
    }

    @Override
    public void visit(CustomBuilt descrptor) throws CopyFormatException {
        if (descrptor != null) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (int i = 0; i < fields.size(); i++) {
                    if (!fields.get(i).isAvailable()) {
                        fields.remove(i);
                    }
                }
            }
            if (fields == null || fields.size() == 0) {
                descrptor.setAvailable(false);
            }
        }
    }

    @Override
    public void visit(Field descrptor, List<Field> availableField) throws CopyFormatException, CopyUnsuccessfullException {

    }


    @Override
    public void visit(Field descrptor) throws CopyFormatException, CopyUnsuccessfullException {

    }

    @Override
    public void visit(CompressorDecorator descrptor) throws CopyFormatException {
        if (descrptor != null) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (int i = 0; i < fields.size(); i++) {
                    if (!fields.get(i).isAvailable()) {
                        fields.remove(i);
                    }
                }
            }
        }
    }

    @Override
    public void visit(MethodDecorator descrptor) throws CopyFormatException {

    }

    @Override
    public void visit(Decorator descrptor) throws CopyFormatException {
        if (descrptor != null) {
            List<CompressorDecorator> cds = descrptor.getCompressorDecorators();
            if (cds != null && cds.size() > 0) {
                for (int i = 0; i < cds.size(); i++) {
                    if (!cds.get(i).isAvailable()) {
                        cds.remove(i);
                    } else {
                        visit(cds.get(i));
                    }
                }
            }
            List<MethodDecorator> mds = descrptor.getMethodDecorators();
            if (mds != null && mds.size() > 0) {
                for (int i = 0; i < mds.size(); i++) {
                    if (!mds.get(i).isAvailable()) {
                        mds.remove(i);
                    } else {
                        visit(mds.get(i));
                    }
                }
            }
            if ((cds == null || cds.size() == 0) && (mds == null || mds.size() == 0)) {
                descrptor.setAvailable(false);
            }
        }
    }
}
