package com.ty.laboratory.objectcopier.copier.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/2
 */
public class CodeStringPair extends StringPair {
    public CodeStringPair(String code, String message) {
        super(code, message);
    }

    public String getCode() {
        return super.getFirst();
    }

    public String getMessage() {
        return super.getSecond();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("code", super.getFirst())
                .append("message", super.getSecond())
                .append("\n")
                .toString();
    }
}
