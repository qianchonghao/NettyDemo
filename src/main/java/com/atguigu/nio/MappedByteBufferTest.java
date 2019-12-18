package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.util.RandomAccess;


public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {

//        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
//        //获取对应的通道
//        FileChannel channel = randomAccessFile.getChannel();
//
        /**
         *  MappedByteBuffer 可让文件直接在内存(堆外内存)修改
         *  【即 buffer-->Server 不需要经过Channel】 直接修改server的内存数据
         *
         *
         * 参数1 FileChannel.MapMode: FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2 position： 0 ： 可以直接修改的起始位置
         * 参数3 size :  5: 是映射到内存的大小(不是索引位置) ,即将 1.txt 的多少个字节映射到内存
         * 可以直接修改的范围就是 0-5
         * 实际类型 DirectByteBuffer
         */
//        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
//
//        mappedByteBuffer.put(0, (byte) 'H');
//        mappedByteBuffer.put(3, (byte) '9');
//        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException
//
//        randomAccessFile.close();
//        System.out.println("修改成功~~");

        RandomAccessFile randomAccessFile = new RandomAccessFile("d://a.txt","rw");
        //RandomAccessFile 可以实现读写？

        FileChannel fileChannel = randomAccessFile.getChannel();
        /**
         *      Channel.map(Mode,pos,range)方法返回MappedByteBuffer
         *
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE,0,5);
        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        mappedByteBuffer.put(5, (byte) 'Y');//IndexOutOfBoundsException
        // 即 位置5不能插入

    }
}
