package com.tianpl.laboratory.objectcopier.copier.utils;


import java.util.List;

/**
 * @project hrc
 * @description desc
 * @auth changtong.tianpl
 * @date 2014/12/22
 */
public class StyleUtil {
    public static String toJson(List<PropertyPair> propertyPairs) {
        String ret = "[";
        for (int i = 0; i < propertyPairs.size(); i++) {
            ret += toJson(propertyPairs.get(i));
            if (i < propertyPairs.size() - 1) {
                ret += ",";
            }
        }
        ret += "]";
        return ret;
    }

    public static String toJson(PropertyPair propertyPair) {
        return "{\"" + propertyPair.getName() + "\",\"" + propertyPair.getValue() + "\"}";
    }

    public static String toKv(List<PropertyPair> propertyPairs) {
        String ret = "";
        for (int i = 0; i < propertyPairs.size(); i++) {
            ret += toKv(propertyPairs.get(i));
            if (i < propertyPairs.size() - 1) {
                ret += ",";
            }
        }
        return ret;
    }

    public static String toKv(PropertyPair propertyPair) {
        return "\"" + propertyPair.getName() + "\":=\"" + propertyPair.getValue() + "\"";
    }
}
