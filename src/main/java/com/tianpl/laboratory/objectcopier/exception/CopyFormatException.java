package com.tianpl.laboratory.objectcopier.exception;
// TODO: Auto-generated Javadoc

/**
 * @project hrc
 * @description 对象拷贝配置格式异常
 * @auth changtong.tianpl
 * @date 2014/12/9
 */
public class CopyFormatException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2239241108350127412L;

    /**
     * Instantiates a new copy format exception.
     */
    public CopyFormatException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CopyFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The Constructor.
     *
     * @param message the message
     */
    public CopyFormatException(String message) {
        super(message);
    }

    /**
     * The Constructor.
     *
     * @param cause the cause
     */
    public CopyFormatException(Throwable cause) {
        super(cause);
    }

}
