package com.atguigu.nio;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{


        /**
         *   当前 后端充当Client，File文件充当 Server
         *    inputStream输出流 将文件-->后端，OutputStream输入流将后端-->文件
         *  1. 流对象内部包含 channel对象
         *      FileChannel = OutPutStream.getChannel();
         *  2. SocketChannel.read(ByteBuffer bb);
         *      Channel读取Buffer数据
         *  3. SocketChannel.write(ByteBuffer bb);
         *      ByteBuffer 写入 Channel
         *  4. SocketChannel.read(ByteBuffer bb);
         *      ByteBuffer 读取 Channel
         */


//        目的1： 实现 client-->buffer--->Channel-->server

//            FileChannel fileChannel = new FileOutputStream("d:\\a.txt").getChannel();

//            ByteBuffer byteBuffer = ByteBuffer.allocate(5);

//        byteBuffer.put("plmm".getBytes());// client -->buffer
//
//            byteBuffer.flip();//定位到buffer 0位置
//
//            fileChannel.write(byteBuffer);//buffer将内容写入Channel
//            // buffer--->Channel-->server
//
//            fileChannel.close();

//        目的2： 实现 server-->channel--->buffer-->client
        File file = new File("d:\\a.txt");
        FileChannel fileChannel = new FileInputStream(file).getChannel();


        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

            fileChannel.read(byteBuffer);// buffer 读取Channel内容
            // server-->channel-->buffer


            System.out.println(new String(byteBuffer.array()));//buffer-->client

    }
}
