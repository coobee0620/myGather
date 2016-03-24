package com.ty.common.lang.enumeration;

/**
 * @project myGather
 * @description ���Ͱ�ȫ��ö������, ����һ��������.
 * @auth changtong.ty
 * @date 2015/6/23
 */
public abstract class LongEnum extends Enum {
    private static final long serialVersionUID = 8152633183977823349L;

    /**
     * ����һ��ö����.
     *
     * @param value ö����������ֵ
     */
    protected static final LongEnum create(long value) {
        return (LongEnum) createEnum(new Long(value));
    }

    /**
     * ����һ��ö����.
     *
     * @param value ö����������ֵ
     */
    protected static final LongEnum create(Number value) {
        return (LongEnum) createEnum(new Long(value.longValue()));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö����������ֵ
     */
    protected static final LongEnum create(String name, long value) {
        return (LongEnum) createEnum(name, new Long(value));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö����������ֵ
     */
    protected static final LongEnum create(String name, Number value) {
        return (LongEnum) createEnum(name, new Long(value.longValue()));
    }

    /**
     * ����һ��ö�����͵�<code>EnumType</code>.
     *
     * @return ö�����͵�<code>EnumType</code>
     */
    protected static Object createEnumType() {
        return new EnumType() {
            protected Class getUnderlyingClass() {
                return Long.class;
            }

            protected Number getNextValue(Number value, boolean flagMode) {
                if (value == null) {
                    return flagMode ? new Long(1)
                            : new Long(0); // Ĭ����ʼֵ
                }

                long longValue = ((Long) value).longValue();

                if (flagMode) {
                    return new Long(longValue << 1); // λģʽ
                } else {
                    return new Long(longValue + 1);
                }
            }

            protected boolean isZero(Number value) {
                return ((Long) value).longValue() == 0L;
            }
        };
    }

    /**
     * ʵ��<code>Number</code>��, ȡ������ֵ.
     *
     * @return ����ֵ
     */
    public int intValue() {
        return ((Long) getValue()).intValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ�ó�����ֵ.
     *
     * @return ������ֵ
     */
    public long longValue() {
        return ((Long) getValue()).longValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>double</code>ֵ.
     *
     * @return <code>double</code>ֵ
     */
    public double doubleValue() {
        return ((Long) getValue()).doubleValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>float</code>ֵ.
     *
     * @return <code>float</code>ֵ
     */
    public float floatValue() {
        return ((Long) getValue()).floatValue();
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת����ʮ�����������ַ���.
     *
     * @return ʮ�����������ַ���
     */
    public String toHexString() {
        return Long.toHexString(((Long) getValue()).intValue());
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɰ˽��������ַ���.
     *
     * @return �˽��������ַ���
     */
    public String toOctalString() {
        return Long.toOctalString(((Long) getValue()).intValue());
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɶ����������ַ���.
     *
     * @return �����������ַ���
     */
    public String toBinaryString() {
        return Long.toBinaryString(((Long) getValue()).intValue());
    }
}

