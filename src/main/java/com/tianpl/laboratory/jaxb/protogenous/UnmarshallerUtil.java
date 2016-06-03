package com.tianpl.laboratory.jaxb.protogenous;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * Created by tianyu on 16/6/3.
 */
public class UnmarshallerUtil {
    @SuppressWarnings("unchecked")
    public static <T> T unmarshalle(String xml, Class<T> valueType) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(valueType);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new StringReader(xml));
    }
}
