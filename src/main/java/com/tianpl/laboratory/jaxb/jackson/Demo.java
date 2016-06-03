package com.tianpl.laboratory.jaxb.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tianyu on 16/6/2.
 */
public class Demo {
    public static void main(String[] args) throws IOException, XMLStreamException {
        XmlMapper xml = new XmlMapper();
        Root root = new Root();
        root.setId("中文");
        root.setName("222");
        root.setSurname("333");

        Level1 level1 = new Level1();
        level1.setL1("L11");
        level1.setL2("L12");

        root.setLevel1(level1);

        System.out.println(beanToXml(root,xml));
        String xmlText = "<xml xmlns=\"\"><name><![CDATA[222]]></name><surname>333</surname><id>中文</id><level1><l1>L11</l1><l2>L12</l2></level1></xml>";
        Root root1 = xmlToBean(xmlText,xml,new TypeReference<Root>(){});
        System.out.println(root1);

        String json = xml2Json(xmlText,xml);
        System.out.println(json);


    }

    public static <T> String beanToXml(T bean,XmlMapper xmlMapper) throws JsonProcessingException {
        return xmlMapper.writeValueAsString(bean);
    }
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xml ,XmlMapper xmlMapper,TypeReference valueTypeRef) throws IOException, XMLStreamException {
        XMLStreamReader reader =
                XMLInputFactory.newInstance().createXMLStreamReader(new BufferedReader(new StringReader(xml)));
        return (T) xmlMapper.readValue(reader,valueTypeRef);
    }

    public static String xml2Json(String xml,XmlMapper xmlMapper) throws IOException, XMLStreamException {
        Map<String,Object> map = xmlToBean(xml, xmlMapper, new TypeReference<HashMap<String,Object>>() {});

        return new ObjectMapper().writeValueAsString(map);
    }
}
