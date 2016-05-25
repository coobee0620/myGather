package com.tianpl.laboratory.excel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Created by tianyu on 16/1/4.
 * @desc 默认的样式模板
 */
public class DefaultStyleTemplate extends StyleTemplate {
    protected enum DefaultStyle {
        Header("default_header"),
        Cell("default_cell");

        DefaultStyle(String key) {
            this.key = key;
        }

        private String key;
        public String key() {
            return key;
        }
    }

    public DefaultStyleTemplate(HSSFWorkbook workbook) {
        super(workbook);
    }

    @Override
    protected void init() {
        // 生成默认的header样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        // 设置这些样式
        headerStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        headerStyle.setFont(font);
        cache.put(DefaultStyle.Header.key(),headerStyle);

        // 生成并设置cell样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        cellStyle.setFont(font2);
        cache.put(DefaultStyle.Cell.key(),cellStyle);
    }

}
