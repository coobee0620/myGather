package com.tianpl.laboratory.io.bio;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by tianyu on 5/18/16.
 */
public class MemoryInput {
    public static void main(String[] args) throws IOException{
        StringReader stringReader = new StringReader(BufferedInputFile.read("src/main/java/com/tianpl/laboratory/io/MemoryInput.java"));
        int c;
        while ((c = stringReader.read()) != -1) {
            if (c == 10) {
                System.out.println((char)c);
            } else {
                System.out.print((char)c);
            }
        }
    }
}
