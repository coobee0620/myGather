package com.tianpl.laboratory.jaxb.protogenous;

import javax.xml.bind.JAXBException;
import java.io.IOException;

/**
 * Created by tianyu on 16/6/2.
 */
public class Demo {
    public static void main(String[] args) throws JAXBException, IOException {
        Root root = new Root();
        root.setId("111");
        root.setName("222");
        root.setSurname("333");

        String xml = MarshallerUtil.marshal(root);
        System.out.println(xml);

//        System.out.println(MarshallerUtil.convertBean2Xml(root,"UTF-8"));


        Root root1 = UnmarshallerUtil.unmarshalle(xml,Root.class);
        System.out.println(root1);
    }
}
