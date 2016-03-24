package com.ty.common.lang.enumeration;

/**
 * @project myGather
 * @description ���Ͱ�ȫ��ö������, ����һ������.
 * @auth changtong.ty
 * @date 2015/6/23
 */
public abstract class IntegerEnum extends Enum {
    private static final long serialVersionUID = 343392921439669443L;

    /**
     * ����һ��ö����.
     *
     * @param value ö����������ֵ
     */
    protected static final IntegerEnum create(int value) {
        return (IntegerEnum) createEnum(new Integer(value));
    }

    /**
     * ����һ��ö����.
     *
     * @param value ö����������ֵ
     */
    protected static final IntegerEnum create(Number value) {
        return (IntegerEnum) createEnum(new Integer(value.intValue()));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö����������ֵ
     */
    protected static final IntegerEnum create(String name, int value) {
        return (IntegerEnum) createEnum(name, new Integer(value));
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö����������ֵ
     */
    protected static final IntegerEnum create(String name, Number value) {
        return (IntegerEnum) createEnum(name, new Integer(value.intValue()));
    }

    /**
     * ����һ��ö�����͵�<code>EnumType</code>.
     *
     * @return ö�����͵�<code>EnumType</code>
     */
    protected static Object createEnumType() {
        return new EnumType() {
            protected Class getUnderlyingClass() {
                return Integer.class;
            }

            protected Number getNextValue(Number value, boolean flagMode) {
                if (value == null) {
                    return flagMode ? new Integer(1)
                            : new Integer(0); // Ĭ����ʼֵ
                }

                int intValue = ((Integer) value).intValue();

                if (flagMode) {
                    return new Integer(intValue << 1); // λģʽ
                } else {
                    return new Integer(intValue + 1);
                }
            }

            protected boolean isZero(Number value) {
                return ((Integer) value).intValue() == 0;
            }
        };
    }

    /**
     * ʵ��<code>Number</code>��, ȡ������ֵ.
     *
     * @return ����ֵ
     */
    public int intValue() {
        return ((Integer) getValue()).intValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ�ó�����ֵ.
     *
     * @return ������ֵ
     */
    public long longValue() {
        return ((Integer) getValue()).longValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>double</code>ֵ.
     *
     * @return <code>double</code>ֵ
     */
    public double doubleValue() {
        return ((Integer) getValue()).doubleValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>float</code>ֵ.
     *
     * @return <code>float</code>ֵ
     */
    public float floatValue() {
        return ((Integer) getValue()).floatValue();
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת����ʮ�����������ַ���.
     *
     * @return ʮ�����������ַ���
     */
    public String toHexString() {
        return Integer.toHexString(((Integer) getValue()).intValue());
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɰ˽��������ַ���.
     *
     * @return �˽��������ַ���
     */
    public String toOctalString() {
        return Integer.toOctalString(((Integer) getValue()).intValue());
    }

    /**
     * ʵ��<code>IntegralNumber</code>��, ת���ɶ����������ַ���.
     *
     * @return �����������ַ���
     */
    public String toBinaryString() {
        return Integer.toBinaryString(((Integer) getValue()).intValue());
    }
}

