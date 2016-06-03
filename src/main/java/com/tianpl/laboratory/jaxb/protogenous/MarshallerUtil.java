package com.tianpl.laboratory.jaxb.protogenous;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.transform.sax.SAXResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianyu on 16/3/29.
 * JAXB helper
 * Bean转xml
 */
public class MarshallerUtil {
    public static String marshal(JAXBModel obj) throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLSerializer serializer = buildXMLSerializer(obj.getClass(),baos);
        SAXResult result = new SAXResult(serializer.asContentHandler());
        jaxbMarshaller.marshal(obj,result);

        return baos.toString();
    }

    public static String convertBean2Xml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            marshaller.setProperty(CharacterEscapeHandler.class.getName(), (CharacterEscapeHandler) (ac, i, j, flag, writer) -> writer.write( ac, i, j ));

            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    private static XMLSerializer buildXMLSerializer(Class<? extends JAXBModel> clz,OutputStream output) {
        Field[] fields = clz.getDeclaredFields();
        List<String> cdataFields = new ArrayList<>();
        for (Field field : fields) {
            if (field.getAnnotation(XmlCDATA.class) != null) {
                XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                String cdataFieldName = "^"+field.getName();
                if (xmlElement != null) {
                    if (!"##default".equals(xmlElement.name())) {
                        cdataFieldName = "^"+xmlElement.name();
                    }
                }
                cdataFields.add(cdataFieldName);
            }
        }

        OutputFormat of = new OutputFormat();
        if (cdataFields.size() > 0)
            of.setCDataElements(cdataFields.toArray(new String[cdataFields.size()]));

        // set any other options you'd like
        of.setPreserveSpace(true);
        of.setIndenting(true);
        // create the serializer
        XMLSerializer serializer = new XMLSerializer(of);
        // set output stream
        serializer.setOutputByteStream(output);

        return serializer;
    }
}
