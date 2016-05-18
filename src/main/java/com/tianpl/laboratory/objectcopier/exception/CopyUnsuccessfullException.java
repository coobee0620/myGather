package com.tianpl.laboratory.objectcopier.exception;

/**
 * @project hrc
 * @description 对象拷贝不成功异常
 * @auth changtong.tianpl
 * @date 2014/12/9
 */
public class CopyUnsuccessfullException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2239241108350127412L;

    /**
     * Instantiates a new copy format exception.
     */
    public CopyUnsuccessfullException() {
        super();
    }

    /**
     * The Constructor.
     *
     * @param message the message
     * @param cause   the cause
     */
    public CopyUnsuccessfullException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The Constructor.
     *
     * @param message the message
     */
    public CopyUnsuccessfullException(String message) {
        super(message);
    }

    /**
     * The Constructor.
     *
     * @param cause the cause
     */
    public CopyUnsuccessfullException(Throwable cause) {
        super(cause);
    }

}
