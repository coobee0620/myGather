package com.tianpl.laboratory.io.nio;

import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by tianyu on 5/22/16.
 */
public class ChannelCopy {
    private static final int BSIZE = 8;
    public static void main(String[] args) throws Exception {
        Assert.isTrue(args.length == 2);
        try (FileChannel  in = new FileInputStream(args[0]).getChannel();
             FileChannel out = new RandomAccessFile("data1.txt","rw").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
            int index = 1;
            while (in.read(buffer) != -1) {
                System.out.println(index++);
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        }
    }
}
