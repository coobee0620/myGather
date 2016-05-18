package com.tianpl.laboratory.objectcopier.copier.utils;


import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;

import java.util.HashSet;
import java.util.Set;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/15
 */
public class TypeMapper {
    public static final String INTEGER_NAME = java.lang.Integer.class.getName();

    public static Set<String> BASIC_DATA_TYPES = new HashSet<String>();
    private static Set<String> AVAILABLE_TARGET_TYPE = new HashSet<String>();

    static {
        BASIC_DATA_TYPES.add(boolean.class.getName());
        BASIC_DATA_TYPES.add(char.class.getName());
        BASIC_DATA_TYPES.add(float.class.getName());
        BASIC_DATA_TYPES.add(double.class.getName());
        BASIC_DATA_TYPES.add(byte.class.getName());
        BASIC_DATA_TYPES.add(short.class.getName());
        BASIC_DATA_TYPES.add(int.class.getName());
        BASIC_DATA_TYPES.add(long.class.getName());

        AVAILABLE_TARGET_TYPE.add(Short.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(Integer.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(Long.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(Boolean.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(Float.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(Double.class.getSimpleName());
        AVAILABLE_TARGET_TYPE.add(String.class.getSimpleName());
    }

    public static boolean isWrapClass(Object obj) {
        System.out.println(obj.getClass().getName());
        return true;
    }


    public static boolean isBasicType(String type) {
        return BASIC_DATA_TYPES.contains(type);
    }

    /**
     * byte-->short
     */
    private static Short toShort(byte b) {
        return (short) b;
    }

    /**
     * String-->short
     */
    private static Short toShort(String s) throws CopyUnsuccessfullException {
        try {
            return Short.valueOf(s);
        } catch (NumberFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    public static Short toShort(Object o) throws CopyUnsuccessfullException {
        if (o instanceof Byte) {
            return toShort(((Byte) o).byteValue());
        } else if (o instanceof Short) {
            return (Short) o;
        } else if (o instanceof String) {
            return toShort(o.toString());
        } else {
            throw new CopyUnsuccessfullException("Type setting error");
        }
    }

    /**
     * byte-->int
     */
    private static Integer toInteger(byte b) {
        return (int) b;
    }

    /**
     * short-->int
     */
    private static Integer toInteger(short s) {
        return (int) s;
    }

    /**
     * String-->int
     */
    private static Integer toInteger(String i) throws CopyUnsuccessfullException {
        try {
            return Integer.valueOf(i);
        } catch (NumberFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    public static Integer toInteger(Object o) throws CopyUnsuccessfullException {
        if (o instanceof Byte) {
            return toInteger(((Byte) o).byteValue());
        } else if (o instanceof Short) {
            return toInteger(((Short) o).shortValue());
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof String) {
            return toInteger(o.toString());
        } else {
            throw new CopyUnsuccessfullException("Type setting error");
        }
    }

    /**
     * byte-->long
     */
    private static Long toLong(byte b) {
        return (long) b;
    }

    /**
     * short-->long
     */
    private static Long toLong(short s) {
        return (long) s;
    }

    /**
     * int-->long
     */
    private static Long toLong(int i) {
        return (long) i;
    }

    /**
     * String-->long
     */
    private static Long toLong(String l) throws CopyUnsuccessfullException {
        try {
            return Long.valueOf(l);
        } catch (NumberFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    public static Long toLong(Object o) throws CopyUnsuccessfullException {
        if (o instanceof Byte) {
            return toLong(((Byte) o).byteValue());
        } else if (o instanceof Short) {
            return toLong(((Short) o).shortValue());
        } else if (o instanceof Integer) {
            return toLong(((Integer) o).intValue());
        } else if (o instanceof Long) {
            return (Long) o;
        } else if (o instanceof String) {
            return toLong(o.toString());
        } else {
            throw new CopyUnsuccessfullException("Type setting error");
        }
    }

    /**
     * String-->boolean
     */
    private static Boolean toBoolean(String b) {
        return Boolean.valueOf(b);
    }

    public static Boolean toBoolean(Object o) {
        if (o instanceof Boolean) {
            return (Boolean) o;
        } else {
            return toBoolean(o.toString());
        }
    }

    /**
     * String-->float
     */
    private static Float toFloat(String f) throws CopyUnsuccessfullException {
        try {
            return Float.valueOf(f);
        } catch (NumberFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    public static Float toFloat(Object o) throws CopyUnsuccessfullException {
        if (o instanceof Float) {
            return (Float) o;
        } else if (o instanceof String) {
            return toFloat(o.toString());
        } else {
            throw new CopyUnsuccessfullException("Type setting error");
        }
    }

    /**
     * float-->double
     */
    private static Double toDouble(float f) {
        return (double) f;
    }

    /**
     * String-->double
     */
    private static Double toDouble(String d) throws CopyUnsuccessfullException {
        try {
            return Double.valueOf(d);
        } catch (NumberFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    private static Double toDouble(Object o) throws CopyUnsuccessfullException {
        if (o instanceof Float) {
            return toDouble(((Float) o).floatValue());
        } else if (o instanceof Double) {
            return (Double) o;
        } else if (o instanceof String) {
            return toDouble(o.toString());
        } else {
            throw new CopyUnsuccessfullException("Type setting error");
        }
    }

    public static String toString(byte b) {
        return String.valueOf(b);
    }

    public static String toString(short s) {
        return String.valueOf(s);
    }

    public static String toString(int i) {
        return String.valueOf(i);
    }

    public static String toString(long l) {
        return String.valueOf(l);
    }

    public static String toString(boolean b) {
        return String.valueOf(b);
    }

    public static String toString(char c) {
        return String.valueOf(c);
    }

    public static String toString(float f) {
        return String.valueOf(f);
    }

    public static String toString(double d) {
        return String.valueOf(d);
    }

    public static String toString(Object o) {
        return o.toString();
    }

    public static String getSimpleName(String type) throws ClassNotFoundException {
        if (type.equals(Byte.class.getName()) || type.equals(byte.class.getName())) {
            return Byte.class.getSimpleName();
        } else if (type.equals(Short.class.getName()) || type.equals(short.class.getName())) {
            return Short.class.getSimpleName();
        } else if (type.equals(Integer.class.getName()) || type.equals(int.class.getName())) {
            return Integer.class.getSimpleName();
        } else if (type.equals(Long.class.getName()) || type.equals(long.class.getName())) {
            return Long.class.getSimpleName();
        } else if (type.equals(Float.class.getName()) || type.equals(float.class.getName())) {
            return Float.class.getSimpleName();
        } else if (type.equals(Double.class.getName()) || type.equals(double.class.getName())) {
            return Double.class.getSimpleName();
        } else if (type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName())) {
            return Boolean.class.getSimpleName();
        } else if (type.equals(Character.class.getName()) || type.equals(char.class.getName())) {
            return Character.class.getSimpleName();
        } else {
            return Class.forName(type).getSimpleName();
        }
    }

    public static boolean isAvailableType(String targetTypeName) {
        return AVAILABLE_TARGET_TYPE.contains(targetTypeName);
    }

    public static boolean checkType(String type, String toMatchType) {
        if (type.equals(Byte.class.getName()) || type.equals(byte.class.getName())) {
            return toMatchType.equals(Byte.class.getName()) || toMatchType.equals(byte.class.getName());
        } else if (type.equals(Short.class.getName()) || type.equals(short.class.getName())) {
            return toMatchType.equals(Short.class.getName()) || toMatchType.equals(short.class.getName());
        } else if (type.equals(Integer.class.getName()) || type.equals(int.class.getName())) {
            return toMatchType.equals(Integer.class.getName()) || toMatchType.equals(int.class.getName());
        } else if (type.equals(Long.class.getName()) || type.equals(long.class.getName())) {
            return toMatchType.equals(Long.class.getName()) || toMatchType.equals(long.class.getName());
        } else if (type.equals(Float.class.getName()) || type.equals(float.class.getName())) {
            return toMatchType.equals(Float.class.getName()) || toMatchType.equals(float.class.getName());
        } else if (type.equals(Double.class.getName()) || type.equals(double.class.getName())) {
            return toMatchType.equals(Double.class.getName()) || toMatchType.equals(double.class.getName());
        } else if (type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName())) {
            return toMatchType.equals(Boolean.class.getName()) || toMatchType.equals(boolean.class.getName());
        } else if (type.equals(Character.class.getName()) || type.equals(char.class.getName())) {
            return toMatchType.equals(Character.class.getName()) || toMatchType.equals(char.class.getName());
        } else {
            return type.equals(toMatchType);
        }
    }
}
