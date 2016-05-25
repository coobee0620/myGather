package com.tianpl.laboratory.excel.handlers;

import com.tianpl.laboratory.excel.ExcelExportedAble;
import org.apache.poi.hssf.usermodel.HSSFCell;

import java.lang.reflect.Field;

/**
 * Created by tianyu on 15/12/31.
 * @desc 单元格处理器
 *      责任链模式
 */
public abstract class CellTypeHandler{
    private Integer order;

    protected CellTypeHandler(Integer order) {
        this.order = order;
    }

    protected CellTypeHandler() {
        this(Integer.MAX_VALUE);
    }


    public abstract <T extends ExcelExportedAble> boolean handle(Field field, T obj, HSSFCell cell);

    public Integer getOrder() {
        return order;
    }
}
