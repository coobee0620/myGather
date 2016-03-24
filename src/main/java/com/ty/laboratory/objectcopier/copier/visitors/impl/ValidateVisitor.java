package com.ty.laboratory.objectcopier.copier.visitors.impl;

import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.constants.ObjectCopierCode;
import com.ty.laboratory.objectcopier.copier.elements.*;
import com.ty.laboratory.objectcopier.copier.utils.CopierUtils;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @project hrc
 * @description ������ģʽ����������ߣ�Concrete Visitor��
 * ����֤��������Ԫ��һ������Ϊnull��reader�͵��÷���֤
 * @auth changtong.ty
 * @date 2014/12/11
 */
public final class ValidateVisitor implements CopierVisitor {
    private final static Log log = LogFactory.getLog(ValidateVisitor.class);

    @Override
    public void visit(Copier descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            if (StringUtils.isBlank(descrptor.getId())) {
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "A copier must be have an id.Please check your configuration.");
            }
            Map<String, Converter> converters = descrptor.getConverters();
            if (converters != null && converters.size() > 0) {
                for (Map.Entry<String, Converter> converterEntry : converters.entrySet()) {
                    visit(converterEntry.getValue());
                }
            } else {
                String errorMsg = "Copier:" + descrptor.getId() + "\n" + "A validate copier at least own one converter." + "\n" + "Will skipped the invalid copier.";
                log.warn(ObjectCopierCode.CONFIG_ERR + errorMsg);
                return;
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(Converter descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            if (descrptor.getFrom() == null) {
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "A converter must be have a source class.");
            }
            if (descrptor.getTo() == null) {
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "A converter must be have a target class.");
            }
            if (descrptor.getIgnores() != null) {
                visit(descrptor.getIgnores());
            }
            if (descrptor.getCustomBuilt() != null) {
                visit(descrptor.getCustomBuilt());
            }
            if (descrptor.getDecorator() != null) {
                visit(descrptor.getDecorator());
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(Ignores descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (Field field : fields) {
                    visit(field);
                }
            } else {
                log.warn("Invalid ignores list.Will be skipped.");
                return;
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(CustomBuilt descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (Field field : fields) {
                    visit(field);
                }
            } else {
                log.warn("Invalid custom built list.Will be skipped.");
                return;
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.CONFIG_ERR);
            throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(Field descrptor, List<Field> availableField) throws CopyFormatException, CopyUnsuccessfullException {
        visit(descrptor);
        availableField.add(descrptor);
    }

    @Override
    public void visit(Field descrptor) throws CopyFormatException {
        if (descrptor != null) {
            switch (descrptor.getParentValidation()) {
                case CUSTOMBUILT:
                    checkCustomBuilt(descrptor);
                    break;
                case IGNORES:
                    CopierUtils.checkName(descrptor.getName());
                    break;
                case COMPRESSOR:
                    CopierUtils.checkName(descrptor.getName());
                    CompressorDecorator cd = (CompressorDecorator) descrptor.getParent();
                    switch (cd.getCompressorType()) {
                        //���
                        case TYPE:
                            CopierUtils.chechActualName(cd.getType(), descrptor.getName());
                            break;
                        case FIELD:
                            Converter converter = descrptor.getConverter();
                            CopierUtils.chechActualName(converter.getFrom(), descrptor.getName());
                            break;
                    }
                    break;
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(CompressorDecorator descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            /**
             * ������target field name
             * ����target field ����ΪString����
             * */
            CopierUtils.checkName(descrptor.getTargetFiledName());
            Converter converter = descrptor.getConverter();
            if (converter == null) {
                log.error(ObjectCopierCode.PARAM_ERR);
                throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + Converter.class.getName() + " should not be null");
            }
            //target field����ΪString
            CopierUtils.checkMatch(converter.getTo(), descrptor.getTargetFiledName(), String.class.getName());

            //���cd.name��cd.typeȫ��Ϊ�ջ�ȫ��Ϊ�ա�����ɶԳ���
            if (StringUtils.isNotBlank(descrptor.getName()) && descrptor.getType() != null) {
                //���converter.from�е�cd.name��ʵ������
                CopierUtils.checkMatch(converter.getFrom(), descrptor.getName(), descrptor.getType().getName());
                descrptor.setCompressorType(CompressorDecorator.CompressorType.TYPE);
            } else if (StringUtils.isBlank(descrptor.getName()) && descrptor.getType() == null) {
                descrptor.setCompressorType(CompressorDecorator.CompressorType.FIELD);
            } else {
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "compressor.name & compressor.type must as a pair.");
            }

            /**
             * ��֤compressor field list
             * */
            List<Field> availableField = new ArrayList<Field>();
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                for (Field field : fields) {
                    field.setParent(descrptor);
                    visit(field, availableField);
                }
            }
            //������ֶ�ѹ��������ôfield list������ֵ
            if (descrptor.getCompressorType() == CompressorDecorator.CompressorType.FIELD && availableField.size() == 0) {
                String errorMsg = "Field compressor have to have a field list.";
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + errorMsg);
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(MethodDecorator descrptor) throws CopyFormatException {
        if (descrptor != null) {
            if (StringUtils.isBlank(descrptor.getType())) {
                String errorMsg = descrptor.getClass().getName() + ":" + "\n" + "must be have a class name about decorate class";
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + errorMsg);
            }
            if (StringUtils.isBlank(descrptor.getMethod())) {
                String errorMsg = descrptor.getClass().getName() + ":" + "\n" + "must be have a method name about decorate class for invoke";
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + errorMsg);
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }
        descrptor.setAvailable(true);
    }

    @Override
    public void visit(Decorator descrptor) throws CopyFormatException, CopyUnsuccessfullException {
        if (descrptor != null) {
            if (descrptor.getMethodDecorators() != null && descrptor.getMethodDecorators().size() > 0) {
                for (MethodDecorator md : descrptor.getMethodDecorators()) {
                    visit(md);
                }
            }
            if (descrptor.getCompressorDecorators() != null && descrptor.getCompressorDecorators().size() > 0) {
                for (CompressorDecorator cd : descrptor.getCompressorDecorators()) {
                    visit(cd);
                }
            }
            descrptor.setAvailable(true);
        }
    }

    private void checkCustomBuilt(Field field) throws CopyFormatException {
        /**
         * ���Բ�ͬ��-���Ƽ�顣
         * ͨ��-���ÿ�����ͬ��ת��
         * */
        CopierUtils.checkName(field.getName());
        CopierUtils.checkName(field.getTargetName());

        /**
         * ���Բ�ͬ����-���ͼ��
         * */
        if (StringUtils.isNotBlank(field.getType()) && StringUtils.isNotBlank(field.getTargetType())) {
            Converter converter = field.getConverter();
            if (converter == null) {
                log.error(ObjectCopierCode.PARAM_ERR);
                throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + Converter.class.getName() + " should not be null");
            }
            //��������е�field.type��Ϊ�գ�����typematch
            CopierUtils.checkMatch(converter.getFrom(), field.getName(), field.getType());
            CopierUtils.checkMatch(converter.getTo(), field.getTargetName(), field.getTargetType());
            field.setDiffType(true);
        }
    }


}
