package com.tianpl.laboratory.jaxb.protogenous;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianyu on 16/3/29.
 * 添加了此标记的Field在用MarshallUtil转换成xml时会自动添加CDATA
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlCDATA {
}
