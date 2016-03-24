package com.ty.laboratory.objectcopier.copier;

import com.ty.laboratory.objectcopier.copier.utils.TypeMapper;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @project hrc
 * @description desc
 * @auth changtong.ty
 * @date 2014/12/17
 */
public class CopyContext {
    private final static Log log = LogFactory.getLog(CopyContext.class);

    public static final String COPY_METHOD_NAME = "copyProperties";

    @Resource
    private CopierDefaultManager copierManager;
    @Resource
    private CopierConfig copierConfig;


    public Method setToTypeMethod(String sourceType, String targettype) throws NoSuchMethodException, CopyFormatException, CopyUnsuccessfullException, ClassNotFoundException {
        //转换类型是已配置的converter
        if (copierConfig.getCopierkey(sourceType, targettype) != null) {
            return this.getClass().getMethod(COPY_METHOD_NAME, Object.class, String.class);
        }
        //基本类型以及基本类型包转类的转化
        String methodPrefix = "to";
        if (TypeMapper.isAvailableType(TypeMapper.getSimpleName(targettype))) {
            return TypeMapper.class.getMethod(methodPrefix + TypeMapper.getSimpleName(targettype), Object.class);
        } else {
            throw new CopyFormatException("There isn't a suitable type covert method.FromType:" + sourceType + "-->ToType:" + targettype);
        }
    }

    public boolean copyProperties(Object source, Object target) throws CopyUnsuccessfullException {
        return copierManager.copyProperties(source, target);
    }

    public boolean copyProperties(Object source, Object target, String copierKey) throws CopyUnsuccessfullException {
        return copierManager.copyProperties(source, target, copierKey);
    }

    public Object copyProperties(Object source, String targetType) throws CopyUnsuccessfullException {
        Object target;

        try {
            target = Class.forName(targetType).newInstance();
            copyProperties(source, target);
        } catch (InstantiationException e) {
            throw new CopyUnsuccessfullException(e);
        } catch (IllegalAccessException e) {
            throw new CopyUnsuccessfullException(e);
        } catch (ClassNotFoundException e) {
            throw new CopyUnsuccessfullException(e);
        }
        return target;
    }
}
