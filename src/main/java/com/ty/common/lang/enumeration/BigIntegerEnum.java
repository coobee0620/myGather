package com.ty.common.lang.enumeration;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public abstract class BigIntegerEnum extends Enum {
    static final long serialVersionUID = 3407019802348379119L;

    /**
     * ����һ��ö����.
     *
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(long value) {
        return (BigIntegerEnum) createEnum(new BigInteger(String.valueOf(value)));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(String name, long value) {
        return (BigIntegerEnum) createEnum(name, new BigInteger(String.valueOf(value)));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(String name, String value) {
        return (BigIntegerEnum) createEnum(name, new BigInteger(value));
    }

    /**
     * ����һ��ö����.
     *
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(BigInteger value) {
        return (BigIntegerEnum) createEnum(value);
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(String name, BigInteger value) {
        return (BigIntegerEnum) createEnum(name, value);
    }

    /**
     * ����һ��ö����.
     *
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(BigDecimal value) {
        return (BigIntegerEnum) createEnum(value.toBigInteger());
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(String name, BigDecimal value) {
        return (BigIntegerEnum) createEnum(name, value.toBigInteger());
    }

    /**
     * ����һ��ö����.
     *
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(Number value) {
        return (BigIntegerEnum) createEnum(new BigInteger(String.valueOf(value)));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö�����ĳ�������ֵ
     */
    protected static final BigIntegerEnum create(String name, Number value) {
        return (BigIntegerEnum) createEnum(name, new BigInteger(String.valueOf(value)));
    }

    /**
     * ����һ��ö�����͵�<code>EnumType</code>.
     *
     * @return ö�����͵�<code>EnumType</code>
     */
    protected static Object createEnumType() {
        return new EnumType() {
            protected Class getUnderlyingClass() {
                return BigInteger.class;
            }

            protected Number getNextValue(Number value, boolean flagMode) {
                if (value == null) {
                    return flagMode ? BigInteger.ONE
                            : BigInteger.ZERO; // Ĭ����ʼֵ
                }

                if (flagMode) {
                    return ((BigInteger) value).shiftLeft(1); // λģʽ
                } else {
                    return ((BigInteger) value).add(BigInteger.ONE);
                }
            }

            protected boolean isZero(Number value) {
                return ((BigInteger) value).equals(BigInteger.ZERO);
            }
        };
    }

    /**
     * ʵ��<code>Number</code>��, ȡ������ֵ.
     *
     * @return ����ֵ
     */
    public int intValue() {
        return ((BigInteger) getValue()).intValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ�ó�����ֵ.
     *
     * @return ������ֵ
     */
    public long longValue() {
        return ((BigInteger) getValue()).longValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>double</code>ֵ.
     *
     * @return <code>double</code>ֵ
     */
    public double doubleValue() {
        return ((BigInteger) getValue()).doubleValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>float</code>ֵ.
     *
     * @return <code>float</code>ֵ
     */
    public float floatValue() {
        return ((BigInteger) getValue()).floatValue();
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת����ʮ�����������ַ���.
     *
     * @return ʮ�����������ַ���
     */
    public String toHexString() {
        return ((BigInteger) getValue()).toString(RADIX_HEX);
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɰ˽��������ַ���.
     *
     * @return �˽��������ַ���
     */
    public String toOctalString() {
        return ((BigInteger) getValue()).toString(RADIX_OCT);
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɶ����������ַ���.
     *
     * @return �����������ַ���
     */
    public String toBinaryString() {
        return ((BigInteger) getValue()).toString(RADIX_BIN);
    }
}

