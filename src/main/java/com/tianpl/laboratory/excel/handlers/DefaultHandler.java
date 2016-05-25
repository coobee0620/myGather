package com.tianpl.laboratory.excel.handlers;

import com.tianpl.laboratory.excel.ExcelExportedAble;
import com.tianpl.laboratory.excel.ExcelExporterException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tianyu on 15/12/31.
 * @desc 默认处理器
 *       始终返回true,使其始终位于处理链的末尾!
 */
public class DefaultHandler extends CellTypeHandler {
    @Override
    public <T extends ExcelExportedAble> boolean handle(Field field, T obj, HSSFCell cell) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(),obj.getClass());
            Method getter = pd.getReadMethod();
            Object value = getter.invoke(obj);
            cell.setCellValue(new HSSFRichTextString(String.valueOf(value)));
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new ExcelExporterException(e.getMessage(),e);
        }
        return true;
    }
}
