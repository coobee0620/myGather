package com.ty.laboratory.objectcopier.copier.elements;


import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.visitors.CopierVisitor;

import java.util.List;

/**
 * @project hrc
 * @description ������ģʽ:����Ԫ�ؽ�ɫ��Concrete Element��
 * @auth changtong.ty
 * @date 2014/12/10
 */
public class CompressorDecorator extends AbstractCopierDescrptor implements Decorate {
    private String name;

    private Class type;

    private Style style = Style.DEFAULT;

    private String targetFiledName;

    private List<Field> fields;

    private Converter converter;

    private CompressorType compressorType;

    public enum Style {
        DEFAULT("default", "standard json"),
        JSON("json", "standard json"),
        KV("kv", "Key-Value")
        /**
         * ��ʱ��֧��������ʽ
         * */
        ;

        private String styleName;
        private String dec;

        private Style(String styleName, String dec) {
            this.styleName = styleName;
            this.dec = dec;
        }

        public String getStyleName() {
            return styleName;
        }

        public void setStyleName(String styleName) {
            this.styleName = styleName;
        }

        public String getDec() {
            return dec;
        }

        public void setDec(String dec) {
            this.dec = dec;
        }

        public static Style getStyle(String styleName) {
            for (Style style1 : values()) {
                if (style1.getStyleName().equalsIgnoreCase(styleName)) {
                    return style1;
                }
            }
            return Style.DEFAULT;
        }
    }

    public enum CompressorType {
        TYPE,//ѹ������Ϊ��ѹ�������ƶ����е��ֶ�ѹ��
        FIELD//ѹ������Ϊ�ֶ�ѹ������converter.from�е��ֶ�ѹ��
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public String getTargetFiledName() {
        return targetFiledName;
    }

    public void setTargetFiledName(String targetFiledName) {
        this.targetFiledName = targetFiledName;
    }

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

    public CompressorType getCompressorType() {
        return compressorType;
    }

    public void setCompressorType(CompressorType compressorType) {
        this.compressorType = compressorType;
    }

    @Override
    public void accept(CopierVisitor visitor) throws CopyFormatException, CopyUnsuccessfullException {
        visitor.visit(this);
    }
}
