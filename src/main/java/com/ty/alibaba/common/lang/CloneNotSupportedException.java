package com.ty.alibaba.common.lang;

import com.ty.alibaba.common.lang.exception.ChainedRuntimeException;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class CloneNotSupportedException extends ChainedRuntimeException {
    private static final long serialVersionUID = 3257281439807584562L;

    /**
     * 构造一个空的异常.
     */
    public CloneNotSupportedException() {
        super();
    }

    /**
     * 构造一个异常, 指明异常的详细信息.
     *
     * @param message 详细信息
     */
    public CloneNotSupportedException(String message) {
        super(message);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     *
     * @param cause 异常的起因
     */
    public CloneNotSupportedException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造一个异常, 指明引起这个异常的起因.
     *
     * @param message 详细信息
     * @param cause 异常的起因
     */
    public CloneNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
