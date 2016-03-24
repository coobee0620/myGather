package com.ty.laboratory.objectcopier.copier.visitors.impl;


import com.ty.laboratory.objectcopier.copier.CopierDefaultManager;
import com.ty.laboratory.objectcopier.copier.elements.*;
import com.ty.laboratory.objectcopier.copier.utils.BeanCopyUtil;
import com.ty.laboratory.objectcopier.copier.utils.CopierUtils;
import com.ty.laboratory.objectcopier.copier.utils.PropertyPair;
import com.ty.laboratory.objectcopier.copier.utils.StyleUtil;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @project hrc
 * @description 访问者模式：具体访问者（Concrete Visitor）
 * 默认访问业务访问者
 * @auth changtong.ty
 * @date 2014/12/11
 */
public final class BeanCopyVisitor implements CopierVisitor {
    private final static Log log = LogFactory.getLog(ValidateVisitor.class);

    private CopierDefaultManager copyManager;

    private Object source;

    private Object target;

    public BeanCopyVisitor(Object source, Object target, CopierDefaultManager copyManager) {
        this.source = source;
        this.target = target;
        this.copyManager = copyManager;
    }

    @Override
    public void visit(Copier descrptor) throws CopyUnsuccessfullException {
        if (source == null || target == null) {
            throw new CopyUnsuccessfullException("No source object or no target object");
        }
        if (descrptor != null && descrptor.isAvailable()) {
            Map<String, Converter> converters = descrptor.getConverters();
            String converterKey = CopierUtils.getConverterKey(source.getClass(), target.getClass());
            if (converters != null && converters.size() > 0 && converters.containsKey(converterKey)) {
                Converter converter = converters.get(converterKey);
                visit(converter);
            } else {
                String errorMsg = "Copier:" + descrptor.getId() + "\n" + "There isn't any converter.Converting failing." + "\n";
                log.error(errorMsg);
            }
        } else {
            String errorMsg = "class:" + this.getClass().getName() + "\n" + "method:" + Thread.currentThread().getStackTrace()[1].getMethodName() + "\n" + "This method should not be have a null parameter.";
            log.error(errorMsg);
            throw new CopyUnsuccessfullException(errorMsg);
        }
    }

    @Override
    public void visit(Converter descrptor) throws CopyUnsuccessfullException {
        /**
         * 主要逻辑
         * */
        if (descrptor != null && descrptor.isAvailable()) {
            Ignores ignores = descrptor.getIgnores();
            visit(ignores);

            CustomBuilt customBuilt = descrptor.getCustomBuilt();
            visit(customBuilt);

            Decorator decorator = descrptor.getDecorator();
            if (decorator != null && decorator.isAvailable()) {
                visit(decorator);
            }
        } else {
            String errorMsg = "Converter:" + descrptor.getId() + "\n" + "It isn't an available converter.Converting failing." + "\n";
            log.error(errorMsg);
            throw new CopyUnsuccessfullException(errorMsg);
        }
    }

    @Override
    public void visit(Field descrptor) throws CopyUnsuccessfullException {
    }

    @Override
    public void visit(Field descrptor, List<Field> availableField) {
        if (descrptor == null || !descrptor.isAvailable()) {
            return;
        }
        if (availableField != null) {
            availableField.add(descrptor);
        }
    }

    @Override
    public void visit(CompressorDecorator descrptor) throws CopyUnsuccessfullException {
        if (descrptor == null || !descrptor.isAvailable()) {
            return;
        }
        List<Field> fields = descrptor.getFields();
        List<PropertyPair> propertyPairs = new ArrayList<PropertyPair>();
        Object invokeObj;
        try {
            if (fields == null) {
                fields = new ArrayList<Field>();
            }
            //拿到invokeObj,用来执行field对应的get方法
            switch (descrptor.getCompressorType()) {
                case TYPE://如果为类型转化的compressor，拿到source中该类型的实例
                    Converter converter = descrptor.getConverter();
                    if (converter == null) {
                        throw new CopyUnsuccessfullException("converter shouldn't be null");
                    }
                    String name = descrptor.getName();
                    Method readMethod = BeanCopyUtil.getMethod(BeanCopyUtil.MethodType.READ, converter.getFrom(), name);
                    invokeObj = readMethod.invoke(source);
                    if (fields.size() == 0) { //如果配置中没有配置field，表示对这个类的所有属性压缩。得到其所有属性
                        PropertyDescriptor[] pds = BeanCopyUtil.getPropertyDescriptors(invokeObj.getClass());
                        for (int i = 0; i < pds.length; i++) {
                            PropertyDescriptor pd = pds[i];
                            if ("class".equals(pd.getName())) {
                                continue;
                            }
                            Field field = new Field();
                            field.setName(pd.getName());
                            field.setConverter(converter);
                            field.setParent(descrptor);
                            field.setAvailable(true);
                            fields.add(field);
                        }
                    }
                    break;
                case FIELD://如果为字段的压缩类型，那么invokeObj就是source
                    invokeObj = source;
                    break;
                default:
                    throw new CopyUnsuccessfullException("No such enum type");
            }

            //得到待解释的Property Pair
            if (fields.size() > 0) {
                for (int i = 0; i < fields.size(); i++) {
                    PropertyPair pair = getPropertiesValue(invokeObj, fields.get(i).getName());
                    if (pair != null) {
                        propertyPairs.add(pair);
                    }
                }
            }
            String compressResult = styleInterpreter(propertyPairs, descrptor.getStyle());
            Method writeMethod = BeanCopyUtil.getMethod(BeanCopyUtil.MethodType.WRITE, target.getClass(), descrptor.getTargetFiledName());
            writeMethod.invoke(target, compressResult);

        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        }
    }

    @Override
    public void visit(MethodDecorator descrptor) throws CopyUnsuccessfullException {
        if (descrptor == null || !descrptor.isAvailable()) {
            return;
        }
    }

    @Override
    public void visit(Decorator descrptor) throws CopyUnsuccessfullException {
        if (descrptor == null || !descrptor.isAvailable()) {
            return;
        }

        //压缩装饰器
        if (descrptor.getCompressorDecorators() != null && descrptor.getCompressorDecorators().size() > 0) {
            for (int i = 0; i < descrptor.getCompressorDecorators().size(); i++) {
                visit(descrptor.getCompressorDecorators().get(i));
            }
        }
        //自定义转换方法
        if (descrptor.getMethodDecorators() != null && descrptor.getMethodDecorators().size() > 0) {
            for (int i = 0; i < descrptor.getMethodDecorators().size(); i++) {
                visit(descrptor.getMethodDecorators().get(i));
            }
        }
    }

    @Override
    public void visit(Ignores descrptor) throws CopyUnsuccessfullException {
        List<String> ignoresFieldName = null;
        if (descrptor != null && descrptor.isAvailable()) {
            List<Field> fields = descrptor.getFields();
            if (fields != null && fields.size() > 0) {
                ignoresFieldName = new ArrayList<String>();
                for (Field field : fields) {
                    if (field.isAvailable()) {
                        ignoresFieldName.add(field.getName());
                    }
                }
            }
        }

        //依赖Spring BeanUtils进行同名拷贝
        BeanCopyUtil.copyProperties(source, target, ignoresFieldName);
    }

    @Override
    public void visit(CustomBuilt descrptor) throws CopyUnsuccessfullException {
        if (descrptor == null || !descrptor.isAvailable()) {
            return;
        }
        List<Field> fields = descrptor.getFields();
        if (fields != null && fields.size() > 0) {
            List<Field> availableField = new ArrayList<Field>();
            for (int i = 0; i < fields.size(); i++) {
                visit(fields.get(i), availableField);
            }
            if (availableField.size() > 0) {
                try {
                    copyProperties4CustomBuilt(source, target, availableField);
                } catch (CopyFormatException e) {
                    throw new CopyUnsuccessfullException(e);
                }
            }
        }
    }

    private PropertyPair getPropertiesValue(Object obj, String propertyName) throws CopyUnsuccessfullException {
        Method readMethod = BeanCopyUtil.getMethod(BeanCopyUtil.MethodType.READ, obj.getClass(), propertyName);
        try {
            Object value = readMethod.invoke(obj);
            if (value != null) {
                return new PropertyPair(propertyName, value.toString());
            } else {
                return new PropertyPair(propertyName, "null");
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        }
    }

    private String styleInterpreter(List<PropertyPair> propertyPairs, CompressorDecorator.Style style) {
        String ret;
        switch (style) {
            case DEFAULT:
                ret = StyleUtil.toJson(propertyPairs);
                break;
            case JSON:
                ret = StyleUtil.toJson(propertyPairs);
                break;
            case KV:
                ret = StyleUtil.toKv(propertyPairs);
                break;
            default://正常永远不会走到这个分支，防止有人变动定义的类型
                ret = StyleUtil.toJson(propertyPairs);
                log.warn("CompressorDecorator.Style may be modified.Use default interpreter.");
        }
        return ret;
    }

    private void copyProperties4CustomBuilt(Object source, Object target, List<Field> customBuiltField) throws CopyUnsuccessfullException, CopyFormatException {
        if (customBuiltField != null && customBuiltField.size() > 0) {
            for (int i = 0; i < customBuiltField.size(); i++) {
                copyPropertiesAllotypic(source, target, customBuiltField.get(i));
            }
        }
    }

    private void copyPropertiesAllotypic(Object source, Object target, Field field) throws CopyUnsuccessfullException, CopyFormatException {
        PropertyDescriptor sourcePD = BeanCopyUtil.getPropertyDescriptor(source.getClass(), field.getName());
        PropertyDescriptor targetPD = BeanCopyUtil.getPropertyDescriptor(target.getClass(), field.getTargetName());
        try {
            if (StringUtils.isNotBlank(field.getTargetName())) {
                Method readMethod = sourcePD.getReadMethod();
                Method writeMethod = targetPD.getWriteMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);

                //不同类型转换
                if (field.isDiffType()) {
                    Method typeMethod = copyManager.setToTypeMethod(sourcePD.getPropertyType().getName(), targetPD.getPropertyType().getName());
                    if (typeMethod.getName().equals(CopierDefaultManager.COPY_METHOD_NAME)) {//非基本转换类型
                        value = typeMethod.invoke(copyManager, value, field.getTargetType());
                    } else {
                        value = typeMethod.invoke(null, value);
                    }
                }

                //执行target的write方法
                writeMethod.invoke(target, value);
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            throw new CopyUnsuccessfullException(e);
        }
    }

    private void copyProperties4Compressor() {

    }

}
