package com.tianpl.laboratory.excel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;


/**
 * Created by tianyu on 15/12/31.
 * @desc ExcelExporter的封装
 *       提供若干简单用法
 */
public class ExcelExporters {
    private final static String Default_Excel_Postfix = ".xls"; //默认的excel扩展名
    public static <T extends ExcelExportedAble>  void export(String fileName, Collection<T> data,Class<T> sheetType) throws FileNotFoundException {
        if (StringUtils.isBlank(fileName)) {
            throw new FileNotFoundException("fileName CANNOT be blank");
        }
        if (StringUtils.countMatches(fileName,".") > 1) {
            throw new FileNotFoundException("there are more than one char (.) in fileName");
        }

        if (!CollectionUtils.isEmpty(data)) {
            //忽略文件的扩展名
            String fullFileName = fileName.split("\\.")[0] + Default_Excel_Postfix;
            OutputStream out = new FileOutputStream(fullFileName);
            ExcelExporter exporter = new ExcelExporter(out);
            ExcelExporter.EESheet<T> sheet1 = exporter.createSheet((sheetType));
            sheet1.produceData(data);
            exporter.export();
        }
    }

    public static <T extends ExcelExportedAble>  void export(OutputStream outputStream, Collection<T> data,Class<T> sheetType) throws FileNotFoundException {
        ExcelExporter exporter = new ExcelExporter(outputStream);
        ExcelExporter.EESheet<T> sheet1 = exporter.createSheet((sheetType));
        sheet1.produceData(data);
        exporter.export();
    }
}
