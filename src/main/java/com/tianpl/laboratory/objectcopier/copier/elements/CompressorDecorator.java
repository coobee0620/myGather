package com.tianpl.laboratory.objectcopier.copier.elements;


import com.tianpl.laboratory.objectcopier.copier.visitors.CopierVisitor;
import com.tianpl.laboratory.objectcopier.exception.CopyFormatException;
import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.List;

/**
 * @project hrc
 * @description 访问者模式:具体元素角色（Concrete Element）
 * @auth changtong.tianpl
 * @date 2014/12/10
 */
public class CompressorDecorator extends AbstractCopierDescrptor {
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
         * 暂时不支持其他样式
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
        TYPE,//压缩类型为类压缩。将制定类中的字段压缩
        FIELD//压缩类型为字段压缩。将converter.from中的字段压缩
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
