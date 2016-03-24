package com.ty.laboratory.objectcopier.copier.utils;

import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/3
 */
public class BeanCopyUtil {

    public static enum MethodType {
        READ,
        WRITE
    }

    /**
     * Copy properties of orig to dest
     * Exception the Entity and Collection Type
     *
     * @param source
     * @param target
     * @param ignores
     * @return the dest bean
     */
    public static void copyProperties(Object source, Object target, List<String> ignores) throws CopyUnsuccessfullException {
        try {
            if (ignores != null && ignores.size() > 0) {
                BeanUtils.copyProperties(source, target, ignores.toArray(new String[ignores.size()]));
            } else {
                BeanUtils.copyProperties(source, target);
            }
        } catch (BeansException e) {
            e.printStackTrace();
            throw new CopyUnsuccessfullException(e);
        }
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws CopyUnsuccessfullException {
        try {
            return BeanUtils.getPropertyDescriptors(clazz);
        } catch (BeansException be) {
            throw new CopyUnsuccessfullException(be);
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) throws CopyUnsuccessfullException {
        try {
            return BeanUtils.getPropertyDescriptor(clazz, propertyName);
        } catch (BeansException be) {
            throw new CopyUnsuccessfullException(be);
        }
    }

    public static Method getMethod(MethodType methodType, Class<?> clazz, String propertyName) throws CopyUnsuccessfullException {
        PropertyDescriptor pd = getPropertyDescriptor(clazz, propertyName);
        Method method;
        switch (methodType) {
            case READ:
                method = pd.getReadMethod();
                break;
            case WRITE:
                method = pd.getWriteMethod();
                break;
            default:
                throw new CopyUnsuccessfullException("No such method.");
        }
        if (!Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
        return method;
    }
}
