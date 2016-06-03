package com.tianpl.laboratory.jaxb.test;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by tianyu on 16/6/2.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String name;

    @XmlJavaTypeAdapter(AdapterCDATA.class)
    private String surname;


    private String id;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("surname", surname)
                .append("id", id)
                .toString();
    }
}
