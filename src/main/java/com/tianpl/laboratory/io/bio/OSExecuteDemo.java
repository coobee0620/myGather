package com.tianpl.laboratory.io.bio;

/**
 * Created by tianyu on 5/20/16.
 */
public class OSExecuteDemo {
    public static void main(String[] args) {
        String clzFile = ClassLoader.getSystemResource("com/tianpl/laboratory/io/").getFile();
        System.out.println(clzFile);
//        OSExecute.command("javap "+clzFile+"OSExecuteDemo.class");
        OSExecute.command("ls -al");
    }
}
