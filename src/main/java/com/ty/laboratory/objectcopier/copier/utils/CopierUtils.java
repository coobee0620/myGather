package com.ty.laboratory.objectcopier.copier.utils;

import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.constants.ObjectCopierCode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/10
 */
public class CopierUtils {
    private final static Log log = LogFactory.getLog(CopierUtils.class);

    public static String getConverterKey(Class source, Class target) {
        return getConverterKey(source.getName(), target.getName());
    }

    public static String getConverterKey(String source, String target) {
        return source + "::" + target;
    }

    public static void checkName(String paraName) throws CopyFormatException {
        if (StringUtils.isBlank(paraName)) {
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + "Missing a name or a target name.");
        }
    }

    public static void checkType(String className) throws CopyFormatException {
        if (StringUtils.isNotBlank(className)) {
            if (!TypeMapper.isBasicType(className)) {
                try {
                    Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new CopyFormatException("Class not Found!", e);
                }
            }
        } else {
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + "className shouldn't be blankValue.");
        }
    }

    /**
     * @param fieldName   clazz中的字段名称
     * @param clazz
     * @param toMatchType 待验证的类型
     * @desc 验证
     */
    public static void checkMatch(Class clazz, String fieldName, String toMatchType) throws CopyFormatException {
        if (StringUtils.isBlank(fieldName) || clazz == null) {
            checkType(toMatchType);
            String errorMsg = "Invalid Parameters";
            log.error(ObjectCopierCode.PARAM_ERR);
            throw new CopyFormatException(ObjectCopierCode.PARAM_ERR + errorMsg);
        }

        try {
            String actualType = BeanCopyUtil.getPropertyDescriptor(clazz, fieldName).getPropertyType().getName();
            if (!TypeMapper.checkType(actualType, toMatchType)) {
                log.error(ObjectCopierCode.CONFIG_ERR);
                throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "Configuration error.actual type don't match for setting type.");
            }
        } catch (CopyUnsuccessfullException e) {
            throw new CopyFormatException(e);
        }
    }

    /**
     * 检查类型中是否有这个属性
     */
    public static void chechActualName(Class clazz, String fieldName) throws CopyFormatException {
        try {
            BeanCopyUtil.getPropertyDescriptor(clazz, fieldName);
        } catch (CopyUnsuccessfullException e) {
            log.error(ObjectCopierCode.CONFIG_ERR);
            throw new CopyFormatException(ObjectCopierCode.CONFIG_ERR + "There isn't property name of " + fieldName + " in " + clazz.getName());
        }
    }
}
