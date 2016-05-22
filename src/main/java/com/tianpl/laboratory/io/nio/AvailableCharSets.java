package com.tianpl.laboratory.io.nio;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by tianyu on 5/22/16.
 */
public class AvailableCharSets {
    public static void main(String[] args) {
        SortedMap<String,Charset> charsets = Charset.availableCharsets();
        for (Map.Entry<String,Charset> charset : charsets.entrySet()) {
            System.out.print(charset.getKey() + "-->");
            System.out.println(charset.getValue());
        }
    }
}
