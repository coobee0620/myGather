package com.tianpl.laboratory.jaxb.test;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.OutputStreamWriter;

/**
 * Created by tianyu on 16/6/2.
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        Root root = new Root();
        root.setId("111");
        root.setName("222");
        root.setSurname("333");
        JAXBContext jc = JAXBContext.newInstance(Root.class);

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(CharacterEscapeHandler.class.getName(), (CharacterEscapeHandler) (ac, i, j, flag, writer) -> writer.write( ac, i, j ));

        marshaller.marshal(root, new OutputStreamWriter(System.out));

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<root>\n" +
                "    <name><![CDATA[222]]></name>\n" +
                "    <surname><![CDATA[333]]></surname>\n" +
                "    <id><![CDATA[111]]></id>\n" +
                "</root>";

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Root root1 = (Root)unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes("utf-8")));
        System.out.println(root1);
    }
}
