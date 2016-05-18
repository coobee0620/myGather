package com.tianpl.laboratory.objectcopier.copier.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/18
 */
public class PropertyPair extends StringPair {

    public PropertyPair(String first, String second) {
        super(first, second);
    }

    public String getName() {
        return this.getFirst();
    }

    public String getValue() {
        return this.getSecond();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", getName())
                .append("value", getValue())
                .append("\n")
                .toString();
    }
}
