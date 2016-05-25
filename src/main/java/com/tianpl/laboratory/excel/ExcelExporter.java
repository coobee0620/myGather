package com.tianpl.laboratory.excel;

import com.tianpl.laboratory.excel.handlers.HandlerChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tianyu on 15/12/30.
 *
 * @desc excel导出器（核心类）
 * @since 1.0
 */
public class ExcelExporter {
    private HSSFWorkbook workbook;
    private OutputStream out;
    private Map<Integer,EESheet> sheetCache = new HashMap<>();

    private int defaultColumnWidth = 15;
    private AtomicInteger sheetCounter = new AtomicInteger(1);

    public ExcelExporter(OutputStream out) {
        workbook = new HSSFWorkbook();
        this.out = out;
    }
    public void setDefaultColumnWidth(int defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
    }

    public class EESheet <T extends ExcelExportedAble> {
        private int idx;
        private HSSFSheet sheet;
        private Class<T> sheetType;

        private String name;
        private List<Field> sortedFields;
        private StyleTemplate styleTemplate;
        private int headerRowIndex;

        public int getIdx() {
            return idx;
        }

        public HSSFSheet getSheet() {
            return sheet;
        }

        public void setSheet(HSSFSheet sheet) {
            this.sheet = sheet;
            initColumnHeader(this.sheet);
        }

        public String getName() {
            return name;
        }

        public Class<T> getSheetType() {
            return sheetType;
        }

        private EESheet(int idx, Class<T> sheetType, HSSFSheet sheet) {
            this(idx,sheetType);
            setSheet(sheet);
        }

        private EESheet(int idx,Class<T> sheetType) {
            this.idx = idx;
            this.sheetType = sheetType;
            initEESheet(sheetType);
        }

        private void initEESheet(Class<T> sheetType) {
            SheetSetting sheetSetting = sheetType.getAnnotation(SheetSetting.class);
            String sheetTitle = sheetType.getSimpleName();
            StyleTemplate styleTemplate = new DefaultStyleTemplate(workbook);
            int headerRowIndex = 0;
            if (sheetSetting != null) {
                //设置sheet title
                if (StringUtils.isNotBlank(sheetSetting.name())) {
                    sheetTitle = sheetSetting.name();
                }
                //初始化样式模板
                if (sheetSetting.styleTemplate() != null) {
                    Class templateClazz = sheetSetting.styleTemplate();
                    if (StyleTemplate.class.isAssignableFrom(templateClazz)) {
                        try {
                            @SuppressWarnings("unchecked")
                            Constructor constructor =  templateClazz.getConstructor(HSSFWorkbook.class);
                            styleTemplate = (StyleTemplate)constructor.newInstance(workbook);
                        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            //TODO log
                            e.printStackTrace();
                        }
                    }
                }
                //header行的索引
                headerRowIndex = sheetSetting.headerRowIndex();
            }
            this.name = sheetTitle;
            this.styleTemplate = styleTemplate;
            this.sortedFields = sortFields(sheetType);
            this.headerRowIndex = headerRowIndex;
        }

        public void produceData(Collection<T> data) {
            if (!CollectionUtils.isEmpty(data)) {
                Iterator<T> it = data.iterator();
                int index = headerRowIndex;
                while (it.hasNext()) {
                    T t = it.next();
                    index++;
                    HSSFRow row = sheet.createRow(index);
                    for (int i = 0; i < sortedFields.size(); i++) {
                        Field field = sortedFields.get(i);
                        HSSFCell cell = row.createCell(i);
                        ColumnSetting columnSetting = field.getAnnotation(ColumnSetting.class);
                        HSSFCellStyle cellStyle = null;
                        if (styleTemplate instanceof DefaultStyleTemplate) {
                            cellStyle = styleTemplate.get(DefaultStyleTemplate.DefaultStyle.Cell.key());
                        }
                        if (columnSetting != null && StringUtils.isNotBlank(columnSetting.cellStyleKey()) && styleTemplate.contains(columnSetting.cellStyleKey())) {
                            cellStyle = styleTemplate.get(columnSetting.cellStyleKey());
                        }
                        if (cellStyle != null) {
                            cell.setCellStyle(cellStyle);
                        }
                        HandlerChain.getInstance().handle(field,t,cell);
                    }
                }
            }
        }

        private void initColumnHeader(HSSFSheet sheet) {
            HSSFRow headerRow = sheet.createRow(headerRowIndex);
            for (int i = 0; i < sortedFields.size(); i++) {
                HSSFCell cell = headerRow.createCell(i);
                ColumnSetting columnSetting = sortedFields.get(i).getDeclaredAnnotation(ColumnSetting.class);
                String headerCellText = sortedFields.get(i).getName();
                HSSFCellStyle style = null;
                if (styleTemplate instanceof DefaultStyleTemplate) {
                    style = styleTemplate.get(DefaultStyleTemplate.DefaultStyle.Header.key());
                }
                if (columnSetting != null) {
                    //设置表头名称
                    if (StringUtils.isNotBlank(columnSetting.name())) {
                        headerCellText = columnSetting.name();
                    }
                    //设置表头样式
                    if (StringUtils.isNotBlank(columnSetting.headerStyleKey()) && styleTemplate.contains(columnSetting.headerStyleKey())) {
                        style = styleTemplate.get(columnSetting.headerStyleKey());
                    }
                }
                if (style != null) {
                    cell.setCellStyle(style);
                }
                cell.setCellValue(new HSSFRichTextString(headerCellText));
            }
        }

        private List<Field> sortFields(Class<T> sheetType) {
            return Stream.of(sheetType.getDeclaredFields()).sorted(
                    (Field f1,Field f2) -> {
                        ColumnSetting header1 = f1.getAnnotation(ColumnSetting.class);
                        ColumnSetting header2 = f2.getAnnotation(ColumnSetting.class);
                        Integer order1 = Integer.MAX_VALUE;
                        Integer order2 = Integer.MAX_VALUE;
                        if (header1 != null) {
                            order1 = header1.order();
                        }
                        if (header2 != null) {
                            order2 = header2.order();
                        }
                        return order1.compareTo(order2);
                    }
            ).collect(Collectors.toList());
        }
    }

    public <T extends ExcelExportedAble> EESheet<T> createSheet(Class<T> sheetType) {
        EESheet<T> eeSheet = new EESheet<>(sheetCounter.getAndIncrement(),sheetType);
        HSSFSheet sheet = workbook.createSheet(eeSheet.getName());
        sheet.setDefaultColumnWidth(defaultColumnWidth);
        eeSheet.setSheet(sheet);
        sheetCache.put(eeSheet.getIdx(),eeSheet);
        return eeSheet;
    }

    /**
     * 按照sheet实际创建顺序获取EESheet
     * @param idx
     * @return
     */
    public EESheet getSheet(int idx) {
        EESheet sheet = sheetCache.get(idx);
        if (sheet == null) {
            throw new ExcelExporterException("请确保获取sheet前通过通过createSheet创建sheet");
        }
        return sheet;
    }

    public void export() {
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
