package com.ty.laboratory.objectcopier.copier;


import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.copier.elements.*;
import com.ty.laboratory.objectcopier.copier.utils.CopierUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @project hrc
 * @description ����beanת�����������ļ�
 * @auth changtong.ty
 * @date 2014/12/9
 */
class ConvertorDigester {
    private static final String DEFAULT_CONFIG_ENCODE = "UTF-8";

    private static final String ELE_CONVERTER = "converter";
    private static final String ELE_CONVERTOR = "convertor";
    private static final String ELE_CUSTOM_BUILT = "customBuilt";
    private static final String ELE_FIELD = "field";
    private static final String ELE_IGNORES = "ignores";
    private static final String ELE_DECORATOR = "decorator";
    private static final String ELE_METHOD_DECORATORS = "methodDecorators";
    private static final String ELE_METHOD_DECORATOR = "methodDecorator";
    private static final String ELE_COMPRESSORS = "compressors";
    private static final String ELE_COMPRESSOR = "compressor";

    private static final String ATR_ID = "id";
    private static final String ATR_TO = "to";
    private static final String ATR_FROM = "from";
    private static final String ATR_NAME = "name";
    private static final String ATR_TYPE = "type";
    private static final String ATR_TARGET_NAME = "targetName";
    private static final String ATR_TARGET_TYPE = "targetType";
    private static final String ATR_STYLE = "style";
    private static final String ATR_METHOD = "method";

    private final static Log log = LogFactory.getLog(ConvertorDigester.class);

    Copier load(InputStream is)
            throws IOException, CopyFormatException {
        return load(is, DEFAULT_CONFIG_ENCODE);
    }

    Copier load(InputStream is, String encode)
            throws IOException, CopyFormatException {
        Copier copier = new Copier();
        InputStreamReader ir = new InputStreamReader(is, encode);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(ir);
            Element root = document.getRootElement();
            copier.setId(root.attributeValue(ATR_ID));
            for (Iterator<?> i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                if (ELE_CONVERTER.equalsIgnoreCase(element.getName()) || ELE_CONVERTOR.equalsIgnoreCase(element.getName())) {
                    try {
                        parseConvertor(copier, element);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        } catch (DocumentException e) {
            throw new CopyFormatException(e);
        }
        return copier;
    }

    private void parseConvertor(Copier copier, Element ele) throws ClassNotFoundException {
        if (copier == null) {
            log.error("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\nadaptorΪ��");
            return;
        }
        if (copier.getConverters() == null) {
            Map<String, Converter> converterMap = new HashMap<String, Converter>();
            copier.setConverters(converterMap);
        }
        /**
         * ����Attribute����
         * */
        String from = ele.attributeValue(ATR_FROM);
        String to = ele.attributeValue(ATR_TO);
        if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            log.warn("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\n��Ч��converter���������ļ��У��Ҳ���converter��from���Ի�to���ԣ�����鿴�����ļ�");
            return;
        }
        Converter converter = new Converter();
        try {
            Class fromClass = Class.forName(from);
            Class toClass = Class.forName(to);

            converter.setId(CopierUtils.getConverterKey(fromClass, toClass));
            converter.setFrom(fromClass);
            converter.setTo(toClass);
        } catch (ClassNotFoundException e) {
            String errorMessage = new StringBuilder()
                    .append("class:")
                    .append(this.getClass().getName()).append("\n")
                    .append("method:")
                    .append(Thread.currentThread().getStackTrace()[1].getMethodName()).append("\n")
                    .append(e.getMessage())
                    .toString();
            log.error(errorMessage);

            throw new ClassNotFoundException(errorMessage, e);
        }
        /**
         * ��Ԫ�ش���
         * */
        for (Iterator<?> i = ele.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            /**
             * ����custom built fields
             * */
            if (ELE_CUSTOM_BUILT.equalsIgnoreCase(element.getName())) {
                CustomBuilt customBuilt = parserCustomBuilt(element, converter);
                if (customBuilt != null) {
                    converter.setCustomBuilt(customBuilt);
                }
            }
            /**
             * ����ignores fields
             * */
            if (ELE_IGNORES.equals(element.getName())) {
                Ignores ignores = parserIgnores(element, converter);
                if (ignores != null) {
                    converter.setIgnores(ignores);
                }
            }
            /**
             * ����decorator
             * */
            if (ELE_DECORATOR.equals(element.getName())) {
                Decorator decorator = parseDecorator(element, converter);
                if (decorator != null) {
                    converter.setDecorator(decorator);
                }
            }
        }
        //��converterװ��Adaptor��convert����������
        copier.getConverters().put(converter.getId(), converter);
    }

    /**
     * ���û��Field����Field�����Ϸ����򷵻ؿ�
     */
    private CustomBuilt parserCustomBuilt(Element ele, Converter converter) {
        CustomBuilt customBuilt = null;
        List<Field> fields = parserFields(ele, Field.ParentValidation.CUSTOMBUILT, converter);
        if (fields != null && fields.size() > 0) {
            customBuilt = new CustomBuilt();
            customBuilt.setFields(fields);
            customBuilt.setConverter(converter);
        }
        return customBuilt;
    }

    /**
     * ���û��Field����Field�����Ϸ����򷵻ؿ�
     */
    private Ignores parserIgnores(Element ele, Converter converter) {
        Ignores ignores = null;
        List<Field> fields = parserFields(ele, Field.ParentValidation.IGNORES, converter);
        if (fields != null && fields.size() > 0) {
            ignores = new Ignores();
            ignores.setConverter(converter);
            ignores.setFields(fields);
        }
        return ignores;
    }

    /**
     * ����Field List
     */
    private List<Field> parserFields(Element ele, Field.ParentValidation parentValidation, Converter converter) {
        List<Field> fields = new ArrayList<Field>();
        for (Iterator<?> i = ele.elementIterator(); i.hasNext(); ) {
            Element customField = (Element) i.next();
            /**
             * ����field
             * */
            if (ELE_FIELD.equalsIgnoreCase(customField.getName())) {
                Field field = parseField(customField, parentValidation, converter);
                if (field != null) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private Field parseField(Element ele, Field.ParentValidation parentValidation, Converter converter) {
        Field field = new Field();
        String name = ele.attributeValue(ATR_NAME);
        if (StringUtils.isBlank(name)) {
            log.error("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\n��Ч��filed��filed��" + ATR_NAME + "�����Ǳ����");
            return null;
        }
        field.setName(name);
        field.setType(ele.attributeValue(ATR_TYPE));
        field.setTargetName(ele.attributeValue(ATR_TARGET_NAME));
        field.setTargetType(ele.attributeValue(ATR_TARGET_TYPE));
        field.setParentValidation(parentValidation);
        field.setConverter(converter);
        return field;
    }

    private Decorator parseDecorator(Element ele, Converter converter) throws ClassNotFoundException {
        Decorator decorator = null;
        for (Iterator<?> i = ele.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            if (ELE_METHOD_DECORATORS.equalsIgnoreCase(element.getName())) {
                List<MethodDecorator> methodDecorators = new ArrayList<MethodDecorator>();
                for (Iterator<?> j = element.elementIterator(); j.hasNext(); ) {
                    Element MDElement = (Element) j.next();
                    if (ELE_METHOD_DECORATOR.equalsIgnoreCase(MDElement.getName())) {
                        MethodDecorator md = parseMethodDecorator(MDElement, converter);
                        if (md != null) {
                            methodDecorators.add(md);
                        }
                    }
                }
                if (methodDecorators.size() > 0) {
                    if (decorator == null) {
                        decorator = new Decorator();
                        decorator.setConverter(converter);
                    }
                    decorator.setMethodDecorators(methodDecorators);
                }
            }
            if (ELE_COMPRESSORS.equalsIgnoreCase(element.getName())) {
                List<CompressorDecorator> compressorDecorators = new ArrayList<CompressorDecorator>();
                for (Iterator<?> j = element.elementIterator(); j.hasNext(); ) {
                    Element CDElement = (Element) j.next();
                    if (ELE_COMPRESSOR.equalsIgnoreCase(CDElement.getName())) {
                        CompressorDecorator cd = parseCompressorDecorator(CDElement, converter);
                        if (cd != null) {
                            compressorDecorators.add(cd);
                        }
                    }

                }
                if (compressorDecorators.size() > 0) {
                    if (decorator == null) {
                        decorator = new Decorator();
                        decorator.setConverter(converter);
                    }
                    decorator.setCompressorDecorators(compressorDecorators);
                }
            }
        }
        return decorator;
    }

    /**
     * �����Ҫ���Բ��Ϸ����򷵻�null
     */
    private CompressorDecorator parseCompressorDecorator(Element ele, Converter converter) throws ClassNotFoundException {
        String fieldName = ele.attributeValue(ATR_NAME);
        String fieldTypeName = ele.attributeValue(ATR_TYPE);
        String targetName = ele.attributeValue(ATR_TARGET_NAME);
        String style = ele.attributeValue(ATR_STYLE);
        if (StringUtils.isBlank(targetName)) {
            log.warn("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\n" + ATR_TARGET_NAME + "Ϊ�գ����������ã�");
            return null;
        }
        Class fieldType = null;
        if (StringUtils.isNotBlank(fieldTypeName)) {
            fieldType = Class.forName(fieldTypeName);
        }

        CompressorDecorator compressorDecorator = new CompressorDecorator();
        compressorDecorator.setConverter(converter);
        compressorDecorator.setName(fieldName);
        compressorDecorator.setType(fieldType);
        compressorDecorator.setTargetFiledName(targetName);
        compressorDecorator.setStyle(CompressorDecorator.Style.getStyle(style));
        List<Field> fields = parserFields(ele, Field.ParentValidation.COMPRESSOR, converter);
        if (fields != null && fields.size() > 0) {
            compressorDecorator.setFields(fields);
        }
        return compressorDecorator;
    }

    private MethodDecorator parseMethodDecorator(Element ele, Converter converter) {
        String className = ele.attributeValue(ATR_NAME);
        String method = ele.attributeValue(ATR_METHOD);
        if (StringUtils.isBlank(className) || StringUtils.isBlank(method)) {
            log.warn("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\n" + ATR_NAME + "����" + ATR_METHOD + "Ϊ�գ����������ã�");
            return null;
        }
        MethodDecorator methodDecorator = new MethodDecorator();
        methodDecorator.setConverter(converter);
        methodDecorator.setType(className);
        methodDecorator.setMethod(method);
        return methodDecorator;
    }

}
