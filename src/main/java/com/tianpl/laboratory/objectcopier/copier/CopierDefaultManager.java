package com.tianpl.laboratory.objectcopier.copier;

import com.tianpl.laboratory.objectcopier.copier.elements.Copier;
import com.tianpl.laboratory.objectcopier.copier.utils.TypeMapper;
import com.tianpl.laboratory.objectcopier.copier.visitors.impl.BeanCopyVisitor;
import com.tianpl.laboratory.objectcopier.exception.CopyFormatException;
import com.tianpl.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * @project hrc
 * @description 访问者模式：client角色
 * 默认的对象拷贝客户端
 * @auth changtong.tianpl
 * @date 2014/12/9
 */
public class CopierDefaultManager {
    public static final String COPY_METHOD_NAME = "copyProperties";
    /**
     * 客户端持有访问者和对象结构角色（Object Structure）
     */

    private final static Log log = LogFactory.getLog(CopierDefaultManager.class);

    private CopierConfig copierConfig;

    public CopierDefaultManager(CopierConfig copierConfig) {
        this.copierConfig = copierConfig;
    }

    /**
     * 推荐使用
     */
    public boolean copyProperties(Object source, Object target, String copierKey) throws CopyUnsuccessfullException {
        if (copierConfig == null) {
            throw new CopyUnsuccessfullException("Factroy is NULL!");
        }
        try {
            BeanCopyVisitor beanCopyVisitor = new BeanCopyVisitor(source, target, this);
            if (source != null && target != null) {
                if (copierConfig.isAvailable() && copierConfig.containsKey(copierKey)) {
                    Copier copier = copierConfig.getCopier(copierKey);
                    copier.accept(beanCopyVisitor);
                } else {
                    String errorMsg = "class:" + this.getClass().getName()
                            + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                            + ";\nThe factory is not available.Or the copierKey is invalid.";
                    log.error(errorMsg);
                    throw new CopyUnsuccessfullException(errorMsg);
                }
            } else {
                log.error("class:" + this.getClass().getName()
                        + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                        + ";\nThe source or the target is null (or both of them are null).");
                return false;
            }
            return true;
        } catch (CopyFormatException e) {
            throw new CopyUnsuccessfullException(e);
        }
    }

    public boolean copyProperties(Object source, Object target) throws CopyUnsuccessfullException {
        if (source != null && target != null) {
            String copierkey = copierConfig.getCopierkey(source.getClass().getName(), target.getClass().getName());
            copyProperties(source, target, copierkey);
        } else {
            log.error("class:" + this.getClass().getName()
                    + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                    + ";\nThe source or the target is null (or both of them are null).");
            return false;
        }
        return true;
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

}
