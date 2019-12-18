package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
//
//        FileInputStream fileInputStream = new FileInputStream("1.txt");
//        FileChannel fileChannel01 = fileInputStream.getChannel();
//
//        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
//        FileChannel fileChannel02 = fileOutputStream.getChannel();
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
//
//        while (true) { //循环读取
//
//            //这里有一个重要的操作，一定不要忘了
//            /*
//             public final Buffer clear() {
//                position = 0;
//                limit = capacity;
//                mark = -1;
//                return this;
//            }
//             */
//            byteBuffer.clear(); //清空buffer
//            int read = fileChannel01.read(byteBuffer);
//            System.out.println("read =" + read);
//            if(read == -1) { //表示读完
//                break;
//            }
//            //将buffer 中的数据写入到 fileChannel02 -- 2.txt
//            byteBuffer.flip();
//            fileChannel02.write(byteBuffer);
//        }
//
//        //关闭相关的流
//        fileInputStream.close();
//        fileOutputStream.close();
        /**
         * 存疑*************************** Channel为什么不体现双向性，可以采用一条channel呢？
         *
         * 目的：使用一个buffer实现 文件copy
         */
        File in = new File("d://a.txt");
        File out = new File("d://b.txt");

        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//初始化 buffer

       while(true){// buffer是一个内存块，每次读取的数据量有限，不能一次读取完整个文件
           byteBuffer.clear();// 清空buffer

           int read = inChannel.read(byteBuffer);// byteBuffer读取inChannel server-->channel-->buffer

           if(read==-1) break;// read表示 buffer读取的字节数，read==-1表示文件读取完毕

           byteBuffer.flip();// pos重定位 读写切换

           outChannel.write(byteBuffer);//byteBuffer 写入channel buffer-->channel-->server

           System.out.println("实现一次buffer内存块大小的复制");
       }


        inChannel.close();
        outChannel.close();



    }
}
