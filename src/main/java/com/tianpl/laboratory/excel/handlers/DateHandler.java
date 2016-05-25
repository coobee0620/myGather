package com.tianpl.laboratory.excel.handlers;


import com.tianpl.common.lang.DateUtil;
import com.tianpl.laboratory.excel.ColumnSetting;
import com.tianpl.laboratory.excel.ExcelExportedAble;
import com.tianpl.laboratory.excel.ExcelExporterException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by yu.tian@mtime.com on 15/12/31.
 * @desc Date类型的处理器
 *       pattern通过ColumnSetting中datePattern设置
 */
public class DateHandler extends CellTypeHandler{
    public DateHandler() {
        super(0);
    }

    @Override
    public <T extends ExcelExportedAble> boolean handle(Field field, T obj, HSSFCell cell) {
        if (Date.class.isAssignableFrom(field.getType())) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(),obj.getClass());
                Method getter = pd.getReadMethod();
                Date value = (Date)getter.invoke(obj);
                ColumnSetting columnSetting = field.getAnnotation(ColumnSetting.class);
                String datePattern = DateUtil.DEFAULT_DATETIME_PATTERN;
                if (columnSetting != null) {
                    datePattern = columnSetting.datePattern();
                }
                cell.setCellValue(new HSSFRichTextString(DateUtil.format(value,datePattern)));
            } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                throw new ExcelExporterException(e.getMessage(),e);
            }
            return true;
        }
        return false;
    }
}
