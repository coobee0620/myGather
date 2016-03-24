package com.ty.common.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @project myGather
 * @description ʵ�ִ˽ӿڵ��쳣, ������һ���쳣�����.
 * @auth changtong.ty
 * @date 2015/6/23
 */
public interface ChainedThrowable extends Serializable {
    /**
     * ȡ���쳣������.
     *
     * @return �쳣������.
     */
    Throwable getCause();

    /**
     * ��ӡ����ջ����׼����.
     */
    void printStackTrace();

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param stream ����ֽ���.
     */
    void printStackTrace(PrintStream stream);

    /**
     * ��ӡ����ջ��ָ�������.
     *
     * @param writer ����ַ���.
     */
    void printStackTrace(PrintWriter writer);

    /**
     * ��ӡ�쳣�ĵ���ջ, �����������쳣����Ϣ.
     *
     * @param writer ��ӡ�������
     */
    void printCurrentStackTrace(PrintWriter writer);
}
