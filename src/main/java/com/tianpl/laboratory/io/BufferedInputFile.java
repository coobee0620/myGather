package com.tianpl.laboratory.io;

import java.io.*;

/**
 * Created by tianyu on 5/18/16.
 */
public class BufferedInputFile {
    public static String read(String fileName) throws IOException {
        String pre = System.getProperty("user.dir");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(pre + File.separator + fileName),"UTF-8"))) {
            String s;
            while ((s = in.readLine()) != null) {
                sb.append(s).append("\n");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(read("src/main/java/com/tianpl/laboratory/io/BufferedInputFile.java"));
    }
}
