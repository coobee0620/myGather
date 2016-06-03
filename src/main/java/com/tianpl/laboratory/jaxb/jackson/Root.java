package com.tianpl.laboratory.jaxb.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.tianpl.laboratory.jaxb.protogenous.JAXBModel;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tianyu on 16/6/2.
 */
@JacksonXmlRootElement(localName = "xml")
public class Root implements JAXBModel{

    @JacksonXmlCData
    private String name;

    private String surname;

    private String id;

    private Level1 level1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Level1 getLevel1() {
        return level1;
    }

    public void setLevel1(Level1 level1) {
        this.level1 = level1;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("surname", surname)
                .append("id", id)
                .append("level1", level1)
                .toString();
    }
}
