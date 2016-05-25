package com.tianpl.laboratory.excel;


import com.tianpl.common.lang.DateUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tianyu on 15/12/30.
 * @desc 用于设置表格
 * @since 1.0
 * @version 1.0
 * @see com.tianpl.laboratory.excel.ExcelExporter.EESheet#sortedFields
 * @see com.tianpl.laboratory.excel.ExcelExporter.EESheet#initEESheet(Class)
 * @see com.tianpl.laboratory.excel.handlers.DateHandler
 * @see ColumnSetting
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnSetting {
    String name() default ""; //如果不设置或者使用默认,则会使用VO字段名作为表头
    int order() default Integer.MAX_VALUE; //用于设置column的顺序,升序排列（数字越大位置越靠后）
    String datePattern() default DateUtil.DEFAULT_DATETIME_PATTERN; //用于设置时间显示的样式.
    String headerStyleKey() default ""; //用于设置表头样式,对应StyleTemplate中的样式key
    String cellStyleKey() default "";//用于设置单元格样式,对应StyleTemplate中的样式key
}
