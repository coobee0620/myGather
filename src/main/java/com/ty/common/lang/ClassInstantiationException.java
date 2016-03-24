package com.ty.common.lang;

import com.ty.common.lang.exception.ChainedException;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class ClassInstantiationException extends ChainedException {
    private static final long serialVersionUID = 3258408422113555761L;

    /**
     * ����һ���յ��쳣.
     */
    public ClassInstantiationException() {
        super();
    }

    /**
     * ����һ���쳣, ָ���쳣����ϸ��Ϣ.
     *
     * @param message ��ϸ��Ϣ
     */
    public ClassInstantiationException(String message) {
        super(message);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param cause �쳣������
     */
    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    /**
     * ����һ���쳣, ָ����������쳣������.
     *
     * @param message ��ϸ��Ϣ
     * @param cause �쳣������
     */
    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}