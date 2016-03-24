package com.ty.common.lang.enumeration;

import com.ty.common.lang.enumeration.internal.EnumConstant;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.*;

/**
 * @project myGather
 * @description desc
 * @auth changtong.ty
 * @date 2015/6/23
 */
public class EnumUtil {
    private static final Map entries = new WeakHashMap();

    /**
     * ȡ��<code>Enum</code>ֵ������.
     *
     * @param enumClass ö������
     *
     * @return <code>Enum</code>ֵ������
     */
    public static Class getUnderlyingClass(Class enumClass) {
        return getEnumType(enumClass).getUnderlyingClass();
    }

    /**
     * �ж�ָ�����Ƶ�ö�����Ƿ񱻶���.
     *
     * @param enumClass ö������
     * @param name ö����������
     *
     * @return �������, �򷵻�<code>true</code>
     */
    public static boolean isNameDefined(Class enumClass, String name) {
        return getEnumType(enumClass).nameMap.containsKey(name);
    }

    /**
     * �ж�ָ��ֵ��ö�����Ƿ񱻶���.
     *
     * @param enumClass ö������
     * @param value ö������ֵ
     *
     * @return �������, �򷵻�<code>true</code>
     */
    public static boolean isValueDefined(Class enumClass, Number value) {
        return getEnumType(enumClass).valueMap.containsKey(value);
    }

    /**
     * ȡ��ָ�����Ƶ�ö����.
     *
     * @param enumClass ö������
     * @param name ö����������
     *
     * @return ö����, ���������, �򷵻�<code>null</code>
     */
    public static Enum getEnumByName(Class enumClass, String name) {
        Enum.EnumType enumType = getEnumType(enumClass);

        if (enumType.enumList.size() != enumType.nameMap.size()) {
            enumType.populateNames(enumClass);
        }

        return (Enum) enumType.nameMap.get(name);
    }

    /**
     * ȡ��ָ��ֵ��ö����.
     *
     * @param enumClass ö������
     * @param value ö������ֵ
     *
     * @return ö����, ���������, �򷵻�<code>null</code>
     */
    public static Enum getEnumByValue(Class enumClass, Number value) {
        return (Enum) getEnumType(enumClass).valueMap.get(value);
    }

    /**
     * ȡ��ָ��ֵ��ö����.
     *
     * @param enumClass ö������
     * @param value ö������ֵ
     *
     * @return ö����, ���������, �򷵻�<code>null</code>
     */
    public static Enum getEnumByValue(Class enumClass, int value) {
        return (Enum) getEnumType(enumClass).valueMap.get(new Integer(value));
    }

    /**
     * ȡ��ָ��ֵ��ö����.
     *
     * @param enumClass ö������
     * @param value ö������ֵ
     *
     * @return ö����, ���������, �򷵻�<code>null</code>
     */
    public static Enum getEnumByValue(Class enumClass, long value) {
        return (Enum) getEnumType(enumClass).valueMap.get(new Long(value));
    }

    /**
     * ȡ��ָ�����͵�����ö������<code>Map</code>, ��<code>Map</code>�������.
     *
     * @param enumClass ö������
     *
     * @return ָ�����͵�����ö������<code>Map</code>
     */
    public static Map getEnumMap(Class enumClass) {
        return Collections.unmodifiableMap(getEnumType(enumClass).nameMap);
    }

    /**
     * ȡ��ָ�����͵�����ö������<code>Iterator</code>.
     *
     * @param enumClass ö������
     *
     * @return ָ�����͵�����ö������<code>Iterator</code>
     */
    public static Iterator getEnumIterator(Class enumClass) {
        return getEnumType(enumClass).enumList.iterator();
    }

    /**
     * ȡ��ָ�����<code>ClassLoader</code>��Ӧ��entry��.
     *
     * @param enumClass <code>Enum</code>��
     *
     * @return entry��
     */
    static Map getEnumEntryMap(Class enumClass) {
        ClassLoader classLoader = enumClass.getClassLoader();
        Map         entryMap    = null;

        synchronized (entries) {
            entryMap = (Map) entries.get(classLoader);

            if (entryMap == null) {
                entryMap = new Hashtable();
                entries.put(classLoader, entryMap);
            }
        }

        return entryMap;
    }

    /**
     * ȡ��<code>Enum</code>���<code>EnumType</code>
     *
     * @param enumClass <code>Enum</code>��
     *
     * @return <code>Enum</code>���Ӧ��<code>EnumType</code>����
     */
    static Enum.EnumType getEnumType(Class enumClass) {
        if (enumClass == null) {
            throw new NullPointerException(EnumConstant.ENUM_CLASS_IS_NULL);
        }

        synchronized (enumClass) {
            if (!Enum.class.isAssignableFrom(enumClass)) {
                throw new IllegalArgumentException(MessageFormat.format(EnumConstant.CLASS_IS_NOT_ENUM,
                        new Object[]{
                                enumClass.getName()
                        }));
            }

            Map      entryMap = getEnumEntryMap(enumClass);
            Enum.EnumType enumType = (Enum.EnumType) entryMap.get(enumClass.getName());

            if (enumType == null) {
                Method createEnumTypeMethod = findStaticMethod(enumClass,
                        EnumConstant.CREATE_ENUM_TYPE_METHOD_NAME,
                        new Class[0]);

                if (createEnumTypeMethod != null) {
                    try {
                        enumType = (Enum.EnumType) createEnumTypeMethod.invoke(null, new Object[0]);
                    } catch (IllegalAccessException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (InvocationTargetException e) {
                    } catch (ClassCastException e) {
                    }
                }

                if (enumType != null) {
                    entryMap.put(enumClass.getName(), enumType);

                    // ��JDK5���棬class loader��ɲ�����ζ�����еĳ�����װ��
                    // ����Ĵ���ǿ��װ�䳣����
                    enumType.populateNames(enumClass);
                }
            }

            if (enumType == null) {
                throw new UnsupportedOperationException(MessageFormat.format(EnumConstant.FAILED_CREATING_ENUM_TYPE,
                        new Object[] {
                                enumClass.getName()
                        }));
            }

            return enumType;
        }
    }

    /**
     * ���ҷ���.
     *
     * @param enumClass ö������
     * @param methodName ������
     * @param paramTypes �������ͱ�
     *
     * @return ��������, ��<code>null</code>��ʾδ�ҵ�
     */
    private static Method findStaticMethod(Class enumClass, String methodName, Class[] paramTypes) {
        Method method = null;

        for (Class clazz = enumClass; !clazz.equals(Enum.class); clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, paramTypes);
                break;
            } catch (NoSuchMethodException e) {
            }
        }

        if ((method != null) && Modifier.isStatic(method.getModifiers())) {
            return method;
        }

        return null;
    }
}

