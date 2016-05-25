package com.tianpl.laboratory.io.bio;

import java.io.*;

/**
 * Created by tianyu on 5/18/16.
 */
public class BasicFileOutput {
    static String file = "BasicFileOutput.out";

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new StringReader(BufferedInputFile.read("src/main/java/com/tianpl/laboratory/io/MemoryInput.java")));
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            int lineCount = 1;
            String s;
            while ((s = in.readLine()) != null) {
                printWriter.println(lineCount++ +": " + s);
            }
        }

        System.out.println(BufferedInputFile.read(file));
    }
}
