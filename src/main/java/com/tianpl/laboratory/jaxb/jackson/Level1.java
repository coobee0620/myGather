package com.tianpl.laboratory.jaxb.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by tianyu on 16/6/3.
 */
@JacksonXmlRootElement(localName = "level1")
public class Level1 {
    private String l1;

    private String l2;

    public String getL1() {
        return l1;
    }

    public void setL1(String l1) {
        this.l1 = l1;
    }

    public String getL2() {
        return l2;
    }

    public void setL2(String l2) {
        this.l2 = l2;
    }
}
