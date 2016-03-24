package com.ty.laboratory.objectcopier.copier;


import com.google.common.collect.ImmutableList;
import com.ty.laboratory.objectcopier.exception.CopyFormatException;
import com.ty.laboratory.objectcopier.exception.CopyUnsuccessfullException;
import com.ty.laboratory.objectcopier.copier.elements.Copier;
import com.ty.laboratory.objectcopier.copier.utils.CopierUtils;
import com.ty.laboratory.objectcopier.copier.visitors.impl.ValidateVisitor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;


import java.io.IOException;
import java.util.*;

/**
 * @project hrc
 * @description ������ģʽ������ṹ��ɫ��Object Structure��
 * @auth changtong.ty
 * @date 2014/12/11
 */
public class CopierConfig {
    private final static Log log = LogFactory.getLog(CopierConfig.class);

    private final Map<String, Copier> factory = new HashMap<String, Copier>();

    private List<Resource> copierConfigs;

    /**
     * �����Ŀ���
     * �ڿͻ��˶Թ�����֤ǰ������������
     */
    private boolean available = false;

    public CopierConfig(List<Resource> copierConfigs) {
        this.copierConfigs = copierConfigs;
    }

    /**
     * ����ʼ��factory�ǲ����õ�
     */
    public void init() {
        if (copierConfigs != null && copierConfigs.size() > 0) {
            if (log.isInfoEnabled()) {
                log.info("Start reading copier configs.");
            }
            ConvertorDigester reader = new ConvertorDigester();
            for (Resource config : copierConfigs) {
                if (log.isInfoEnabled()) {
                    log.info("Reading config:" + config.toString());
                }
                try {
                    String copierKey;
                    Copier copier = reader.load(config.getInputStream());
                    //���ÿ�������ļ���copier�����ˣ�����Ϊfactory�е�key������ʹ�������ļ��ľ��Ե�ַ��Ϊkey
                    if (StringUtils.isNotBlank(copier.getId())) {
                        copierKey = copier.getId();
                    } else {
                        copierKey = config.getURL().getFile();
                        copier.setId(copierKey);
                    }
                    putCopier(copierKey, copier);
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO
                } catch (CopyFormatException e) {
                    e.printStackTrace();
                    //TODO
                }
                if (log.isInfoEnabled()) {
                    log.info("Ending reading copier configs.");
                }
            }
        } else {
            log.warn("The copier init unsuccessful.There isn't any config file.");
        }
        ValidateVisitor validateVisitor = new ValidateVisitor();
        validateFactory(validateVisitor);
    }

    /**
     * ��֤����
     * 1.�������copier����֤��ͨ�������������á�
     * 2.�����һ��copier�ܹ���֤ͨ�����������á�
     */
    public void validateFactory(ValidateVisitor validateVisitor) {
        boolean validate = false;
        for (Map.Entry<String, Copier> copierEntry : this.entrySet()) {
            //ÿ��ѭ������֤һ��copier������Ѿ���validate��Ϊtrue��ô�����ٴν����֧��
            if (validateCopier(copierEntry.getKey(), copierEntry.getValue(), validateVisitor) && !validate) {
                validate = true;
            }
        }
        setAvailable(validate);
    }

    private boolean validateCopier(String copierId, Copier copier, ValidateVisitor validateVisitor) {
        try {
            if (copier != null) {
                copier.accept(validateVisitor);
            } else {
                log.error("class:" + this.getClass().getName()
                        + ";\nmethod:" + Thread.currentThread().getStackTrace()[1].getMethodName()
                        + ";\nThis method should not be have a null parameter.");
                return false;
            }
        } catch (CopyFormatException e) {
            String errorMeg = new StringBuilder()
                    .append("Copier:")
                    .append(copierId).append("\n")
                    .append("To validate unsuccessful.")
                    .append("Exception message:")
                    .append(e.getMessage())
                    .toString();
            log.error(errorMeg);
            return false;
        } catch (Exception e) {
            String errorMeg = new StringBuilder()
                    .append("Catch a exception when validate��").append("\n")
                    .append(e.getMessage())
                    .toString();
            log.error(errorMeg);
        }
        return true;
    }

    public Set<Map.Entry<String, Copier>> entrySet() {
        return factory.entrySet();
    }

    private void putCopier(String copierKey, Copier copier) {
        factory.put(copierKey, copier);
    }

    public Copier getCopier(String copierKey) {
        if (factory.containsKey(copierKey)) {
            return factory.get(copierKey);
        }
        return null;
    }

    /**
     * ����factory�е�copiers��key
     * all=true������ȫ��keys
     * all=false,ֻ���ؿ���copier��keys
     */
    public List<String> getCopierKeys(boolean all) {
        List keys = null;
        if (available && factory != null && factory.size() > 0) {
            if (all) {
                keys = ImmutableList.copyOf(factory.keySet());
            } else {
                List<String> availableKey = new ArrayList<String>();
                for (Map.Entry<String, Copier> copierEntry : factory.entrySet()) {
                    if (copierEntry.getValue().isAvailable()) {
                        availableKey.add(copierEntry.getKey());
                    }
                }
                if (availableKey.size() > 0) {
                    keys = ImmutableList.copyOf(availableKey);
                }
            }
        }
        return keys;
    }

    /**
     * ����ת�������ҵ���Ӧ��copierKey
     */
    public String getCopierkey(String sourceType, String targetType) throws CopyUnsuccessfullException {
        if (factory == null) {
            throw new CopyUnsuccessfullException("Factroy is NULL!");
        }
        String coverterKey = CopierUtils.getConverterKey(sourceType, targetType);
        if (this.isAvailable()) {
            for (Map.Entry<String, Copier> copierEntry : this.entrySet()) {
                Copier copier = copierEntry.getValue();
                if (copier.isAvailable() && copier.getConverters().containsKey(coverterKey)) {
                    return copierEntry.getKey();
                }
            }
        }
        return null;
    }

    public boolean containsKey(String key) {
        return factory.containsKey(key);
    }

    public boolean isAvailable() {
        return available;
    }

    private void setAvailable(boolean available) {
        this.available = available;
    }
}
