package com.ty.common.lang.enumeration;

import com.ty.common.lang.ClassLoaderUtil;
import com.ty.common.lang.StringUtil;
import com.ty.common.lang.enumeration.internal.EnumConstant;
import com.ty.common.lang.enumeration.internal.NumberType;

import java.io.InvalidClassException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.*;

/**
 * @project myGather
 * @description ���Ͱ�ȫ��ö������.
 * @auth changtong.ty
 * @date 2015/6/23
 */
public abstract class Enum extends Number implements NumberType, Comparable, Serializable {
    private static final long serialVersionUID = -3420208858441821772L;
    private String            name;
    private Number            value;

    /* ============================================================================ */
    /*  ����enum�ͳ�ʼ��������                                                      */
    /* ============================================================================ */
    /**
     * ����һ��ö������
     */
    protected Enum() {
    }

    /**
     * ����һ��ö������<p>ö���������ƺͳ���������ͬ����ֵ�����Զ�������</p>
     */
    protected static final Enum create() {
        return createEnum(null, null, false);
    }

    /**
     * ����һ��ö������<p>ö���������ƺͳ���������ͬ����ֵ�����Զ�������</p>
     *
     * @param name ö����������
     */
    protected static final Enum create(String name) {
        return createEnum(name, null, false);
    }

    /**
     * ����һ��ö����, ������ָ����ֵ.
     *
     * @param value ö������ֵ, ���ֵ����Ϊ<code>null</code>
     */
    static final Enum createEnum(Number value) {
        return createEnum(null, value, true);
    }

    /**
     * ����һ��ö����, ������ָ����ֵ.
     *
     * @param name ö����������
     * @param value ö������ֵ, ���ֵ����Ϊ<code>null</code>
     */
    static final Enum createEnum(String name, Number value) {
        return createEnum(name, value, true);
    }

    /**
     * ����һ��ö����.
     *
     * @param name ö����������
     * @param value ö������ֵ
     * @param withValue �����<code>true</code>, ���ö����������ָ����ֵ, �����ö������������һ���Զ�������ֵ
     */
    private static Enum createEnum(String name, Number value, boolean withValue) {
        String enumClassName                   = null;
        Class  enumClass;
        Enum   enumObject;

        try {
            enumClassName                      = getCallerClassName();
            enumClass                          = ClassLoaderUtil.loadClass(enumClassName);
            enumObject                         = (Enum) enumClass.newInstance();

            enumObject.setName(StringUtil.trimToNull(name));
        } catch (ClassNotFoundException e) {
            throw new CreateEnumException("Could not find enum class " + enumClassName, e);
        } catch (Exception e) {
            throw new CreateEnumException("Could not instantiate enum instance of class "
                    + enumClassName, e);
        }

        if (withValue && (value == null)) {
            throw new NullPointerException(EnumConstant.ENUM_VALUE_IS_NULL);
        }

        EnumType enumType = EnumUtil.getEnumType(enumClass);
        boolean  flagMode = enumObject instanceof Flags;

        if (withValue) {
            enumObject.value = enumType.setValue(value, flagMode);
        } else {
            enumObject.value = enumType.getNextValue(flagMode);
        }

        // ��enum����enumList��
        enumType.enumList.add(enumObject);

        // ��enum����valueMap, ����ж��enum��ֵ��ͬ, ��ȡ��һ��
        if (!enumType.valueMap.containsKey(enumObject.value)) {
            enumType.valueMap.put(enumObject.value, enumObject);
        }

        // ��enum����nameMap, ����ж��enum��������ͬ, ��ȡ��һ��
        if ((enumObject.name != null) && !enumType.nameMap.containsKey(enumObject.name)) {
            enumType.nameMap.put(enumObject.name, enumObject);
        }

        return enumObject;
    }

    /**
     * ȡ�õ����ߵ�������
     *
     * @return ����������
     */
    private static String getCallerClassName() {
        StackTraceElement[] callers   = new Throwable().getStackTrace();
        String              enumClass = Enum.class.getName();

        for (int i = 0; i < callers.length; i++) {
            StackTraceElement caller     = callers[i];
            String            className  = caller.getClassName();
            String            methodName = caller.getMethodName();

            if (!enumClass.equals(className) && "<clinit>".equals(methodName)) {
                return className;
            }
        }

        throw new CreateEnumException("Cannot get Enum-class name");
    }

    /* ============================================================================ */
    /*  Enum��Ա������                                                              */
    /* ============================================================================ */
    /**
     * ȡ��ö����������.
     *
     * @return ö����������
     */
    public String getName() {
        if (name == null) {
            Class    enumClass = ensureClassLoaded();
            EnumType enumType  = EnumUtil.getEnumType(enumClass);

            enumType.populateNames(enumClass);
        }

        return name;
    }

    /**
     * ����ö���������ơ�<p>��������Ѿ������ã��÷������׳��쳣��</p>
     *
     * @param name ö����������
     *
     * @return ��ǰenum
     *
     * @throws IllegalStateException ��������Ѿ�������
     */
    private Enum setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("Enum name already set: " + this.name);
        }

        this.name = name;

        return this;
    }

    /**
     * ȡ��ö������ֵ.
     *
     * @return ö������ֵ
     */
    public Number getValue() {
        return value;
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>byte</code>ֵ.
     *
     * @return <code>byte</code>ֵ
     */
    public byte byteValue() {
        return (byte) intValue();
    }

    /**
     * ʵ��<code>Number</code>��, ȡ��<code>short</code>ֵ.
     *
     * @return <code>short</code>ֵ
     */
    public short shortValue() {
        return (short) intValue();
    }

    /**
     * ����һ��ö�����Ƚϴ�С, ���ǰ�ö������ֵ�Ƚ�.
     *
     * @param otherEnum Ҫ�Ƚϵ�ö����
     *
     * @return �������<code>0</code>, ��ʾֵ���, ����<code>0</code>��ʾ��ǰ��ö������ֵ��<code>otherEnum</code>��,
     *         С��<code>0</code>��ʾ��ǰ��ö������ֵ��<code>otherEnum</code>С
     */
    public int compareTo(Object otherEnum) {
        if (!getClass().equals(otherEnum.getClass())) {
            throw new CreateEnumException(MessageFormat.format(EnumConstant.COMPARE_TYPE_MISMATCH,
                    new Object[]{
                            getClass().getName(),
                            otherEnum.getClass().getName()
                    }));
        }

        return ((Comparable) value).compareTo(((Enum) otherEnum).value);
    }

    /**
     * �Ƚ�����ö�����Ƿ����, ��: ������ͬ, ����ֵ��ͬ(�����ֿ��Բ�ͬ).
     *
     * @param obj Ҫ�ȽϵĶ���
     *
     * @return ������, �򷵻�<code>true</code>
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((obj == null) || !getClass().equals(obj.getClass())) {
            return false;
        }

        return value.equals(((Enum) obj).value);
    }

    /**
     * ȡ��ö������hashֵ.  �������ö������ͬ, �����ǵ�hashֵһ����ͬ.
     *
     * @return hashֵ
     */
    public int hashCode() {
        return getClass().hashCode() ^ value.hashCode();
    }

    /**
     * ��ö����ת�����ַ���, Ҳ����ö����������.
     *
     * @return ö����������
     */
    public String toString() {
        return getName();
    }

    /**
     * ȡ���ѱ�װ�ص�class��
     *
     * @return ��ǰ�����class
     */
    public Class ensureClassLoaded() {
        Class enumClass = getClass();

        synchronized (enumClass) {
            return enumClass;
        }
    }

    /**
     * ��"���л�"���̵���, ����ö������singleton.
     *
     * @return ö������singleton
     *
     * @throws java.io.ObjectStreamException ������л�����
     */
    protected Object writeReplace() throws ObjectStreamException {
        getName();
        return this;
    }

    /**
     * ��"�����л�"���̵���, ȷ������ö������singleton.
     *
     * @return ö������singleton
     *
     * @throws ObjectStreamException ��������л�����
     */
    protected Object readResolve() throws ObjectStreamException {
        Class    enumClass  = ensureClassLoaded();
        EnumType enumType   = EnumUtil.getEnumType(enumClass);
        Enum     enumObject = (Enum) enumType.nameMap.get(getName());

        if (enumObject == null) {
            enumType.populateNames(enumClass);
            enumObject = (Enum) enumType.nameMap.get(getName());
        }

        if (enumObject == null) {
            throw new InvalidClassException("Enum name \"" + getName() + "\" not found in class "
                    + enumClass.getName());
        }

        if (!enumObject.value.equals(value)) {
            throw new InvalidClassException("Enum value \"" + value + "\" does not match in class "
                    + enumClass.getName());
        }

        return enumObject;
    }

    /**
     * ����һ��ö�����͵Ķ�����Ϣ.
     */
    protected abstract static class EnumType {
        private Number value;
        final Map nameMap  = Collections.synchronizedMap(new HashMap());
        final Map      valueMap = Collections.synchronizedMap(new HashMap());
        final List enumList = new ArrayList();

        /**
         * ����ָ��ֵΪ��ǰֵ.
         *
         * @param value ��ǰֵ
         * @param flagMode �Ƿ�Ϊλģʽ
         *
         * @return ��ǰֵ
         */
        final Number setValue(Number value, boolean flagMode) {
            this.value = value;

            return value;
        }

        /**
         * ȡ����һ��ֵ.
         *
         * @param flagMode �Ƿ�Ϊλģʽ
         *
         * @return ��ǰֵ
         */
        final Number getNextValue(boolean flagMode) {
            value = getNextValue(value, flagMode);

            if (flagMode && isZero(value)) {
                throw new UnsupportedOperationException(EnumConstant.VALUE_OUT_OF_RANGE);
            }

            return value;
        }

        /**
         * ʹ�÷���ķ�ʽװ��enum�����ơ�
         *
         * @param enumClass enum��
         */
        final void populateNames(Class enumClass) {
            synchronized (enumClass) {
                Field[] fields = enumClass.getFields();

                for (int i = 0; i < fields.length; i++) {
                    Field field    = fields[i];
                    int   modifier = field.getModifiers();

                    if (Modifier.isPublic(modifier) && Modifier.isFinal(modifier)
                            && Modifier.isStatic(modifier)) {
                        try {
                            Object value = field.get(null);

                            for (Iterator j = valueMap.values().iterator(); j.hasNext();) {
                                Enum enumObject = (Enum) j.next();

                                if ((value == enumObject) && (enumObject.name == null)) {
                                    enumObject.name = field.getName();
                                    nameMap.put(enumObject.name, enumObject);
                                    break;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            throw new CreateEnumException(e);
                        }
                    }
                }
            }
        }

        /**
         * ȡ��<code>Enum</code>ֵ������.
         *
         * @return <code>Enum</code>ֵ������
         */
        protected abstract Class getUnderlyingClass();

        /**
         * ȡ��ָ��ֵ����һ��ֵ.
         *
         * @param value ָ��ֵ
         * @param flagMode �Ƿ�Ϊλģʽ
         *
         * @return ���<code>value</code>Ϊ<code>null</code>, �򷵻�Ĭ�ϵĳ�ʼֵ, ���򷵻���һ��ֵ
         */
        protected abstract Number getNextValue(Number value, boolean flagMode);

        /**
         * �ж��Ƿ�Ϊ<code>0</code>.
         *
         * @param value Ҫ�жϵ�ֵ
         *
         * @return �����, �򷵻�<code>true</code>
         */
        protected abstract boolean isZero(Number value);
    }
}
