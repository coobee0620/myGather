package com.tianpl.laboratory.jaxb.test;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by tianyu on 16/6/2.
 */
public class AdapterCDATA extends XmlAdapter<String, String> {
    @Override
    public String unmarshal(String v) throws Exception {
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
        return "<![CDATA[" + v + "]]>";
    }
}
