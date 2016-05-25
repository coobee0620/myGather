package com.tianpl.laboratory.excel;

/**
 * Created by tianyu on 15/12/31.
 */
public class ExcelExporterException extends RuntimeException {
    public ExcelExporterException(String message) {
        super(message);
    }

    public ExcelExporterException(String message, Throwable cause) {
        super(message, cause);
    }
}
