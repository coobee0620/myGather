package com.tianpl.laboratory.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianyu on 16/1/4.
 * @desc 样式模板基类,其子类用于设置单元格样式.（此类不需要显示的实例化)
 *       通过init()利用workbook创建样式并通过styleKey保存在cache中.
 *       由SheetSetting为sheet中的styleTemplate设置,如果不设置将使用DefaultStyleTemplate作为默认的样式模板
 *       由ColumnSetting的ColumnSetting#headerStyleKey和ColumnSetting#cellStyleKey通过字符串key来应用模板中样式
 * @see ColumnSetting
 * @see SheetSetting
 * @see DefaultStyleTemplate
 */
public abstract class StyleTemplate {
    protected HSSFWorkbook workbook;

    protected Map<String,HSSFCellStyle> cache = new HashMap<>();
    public StyleTemplate(HSSFWorkbook workbook) {
        this.workbook = workbook;
        init();
    }

    public HSSFCellStyle get(String styleKey) {
        return cache.get(styleKey);
    }

    public boolean contains(String styleKey) {
        return cache.containsKey(styleKey);
    }

    protected abstract void init();
}
