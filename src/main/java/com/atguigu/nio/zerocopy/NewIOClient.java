package com.atguigu.nio.zerocopy;


import java.io.FileInputStream;

import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

//在linux下一个transferTo 方法就可以完成传输
//在windows 下 一次调用 transferTo 只能发送8m , 就需要分段传输文件, 而且要主要
//传输时的位置 =》 课后思考...
//transferTo 底层使用到零拷贝
public class NewIOClient {
    public static void main(String[] args) throws Exception {

//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress("localhost", 8005));
//        String filename = "D:\\a.txt";
//
//        //得到一个文件channel
//        FileChannel fileChannel = new FileInputStream(filename).getChannel();
//
//        //准备发送
//        long startTime = System.currentTimeMillis();
//
//        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
//
//        System.out.println("发送的总的字节数 =" + transferCount + " 耗时:" + (System.currentTimeMillis() - startTime));
//
//        //关闭


//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress("localhost", 8005));
//        String filename = "D:\\a.txt";
//
//        //得到一个文件channel
//        FileChannel fileChannel = new FileInputStream(filename).getChannel();
//
//        //准备发送
//        long startTime = System.currentTimeMillis();
//
//        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
//
//        System.out.println("发送的总的字节数 =" + transferCount + " 耗时:" + (System.currentTimeMillis() - startTime));
//        fileChannel.close();
    SocketChannel socketChannel = SocketChannel.open();
    socketChannel.connect(new InetSocketAddress("localhost",8005));

    FileChannel fileChannel = new FileInputStream("D:\\a.txt").getChannel();

    long start = System.currentTimeMillis();

    long transCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);

    System.out.println("发送总字节数："+transCount+"总时长"+ (System.currentTimeMillis()-start));
        fileChannel.close();

    }
}
