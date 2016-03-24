package com.ty.common.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class ChainedException extends Exception
        implements ChainedThrowable {
    private static final long      serialVersionUID = 3257568390900888633L;
    private final ChainedThrowable delegate = new ChainedThrowableDelegate(this);
    private Throwable              cause;

    /**
     * ����һ���յ��쳣.
     */
    public ChainedException() {
        super();
    }

    /**
     * ����һ���쳣, ָ���쳣����ϸ��Ϣ.
     *
     * @param message ��ϸ��Ϣ
     */
    public ChainedException(String message) {
        super(message);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param cause �쳣������
     */
    public ChainedException(Throwable cause) {
        super((cause == null) ? null
                : cause.getMessage());
        this.cause = cause;
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param message ��ϸ��Ϣ
     * @param cause �쳣������
     */
    public ChainedException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    /**
     * ȡ����������쳣������.
     *
     * @return �쳣������.
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * ��ӡ����ջ����׼����.
     */
    public void printStackTrace() {
        delegate.printStackTrace();
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param stream ����ֽ���.
     */
    public void printStackTrace(PrintStream stream) {
        delegate.printStackTrace(stream);
    }

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param writer ����ַ���.
     */
    public void printStackTrace(PrintWriter writer) {
        delegate.printStackTrace(writer);
    }

    /**
     * ��ӡ�쳣�ĵ���ջ, �����������쳣����Ϣ.
     *
     * @param writer ��ӡ�������
     */
    public void printCurrentStackTrace(PrintWriter writer) {
        super.printStackTrace(writer);
    }
}

