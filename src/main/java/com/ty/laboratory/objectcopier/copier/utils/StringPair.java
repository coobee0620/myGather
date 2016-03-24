package com.ty.laboratory.objectcopier.copier.utils;

/**
 * @project hrc
 * @description 来自com.sun.corba.se.spi.orb.StringPair
 * 将getFirst和getSecond设置为protected，对子类的使用者屏蔽这两个方法
 * 便于子类重写这两个方法时重写成业务相关的名称
 * @auth changtong.ty
 * @date 2014/12/2
 */
public class StringPair {
    private String first;
    private String second;

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof StringPair))
            return false;

        StringPair other = (StringPair) obj;

        return (first.equals(other.first) &&
                second.equals(other.second));
    }

    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    protected String getFirst() {
        return first;
    }

    protected String getSecond() {
        return second;
    }


}
