package com.tianpl.laboratory.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianyu on 15/12/30.
 * @desc sheet纬度
 *      name 用于设置sheet的title.如果不设置将使用继承ExcelExportedAble了的数据元素类名作为title设置
 *      styleTemplate 用于指定该sheet使用的样式模板（StyleTemplate.class）
 *      headerRowIndex header所在行索引
 * @since 1.0
 * @version
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetSetting {
    String name() default "";
    Class styleTemplate() default DefaultStyleTemplate.class;
    int headerRowIndex() default 0;
}
