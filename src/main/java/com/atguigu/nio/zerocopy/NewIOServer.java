package com.atguigu.nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

//服务器
public class NewIOServer {
    public static void main(String[] args) throws Exception {

//        InetSocketAddress address = new InetSocketAddress(8005);
//
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//
//        ServerSocket serverSocket = serverSocketChannel.socket();
//
//        serverSocket.bind(address);
//
//        //创建buffer
//        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
//
//        while (true) {
//            SocketChannel socketChannel = serverSocketChannel.accept();
//
//            int readcount = 0;
//            while (-1 != readcount) {
//                try {
//
//                    readcount = socketChannel.read(byteBuffer);
//
//                }catch (Exception ex) {
//                   // ex.printStackTrace();
//                    break;
//                }
//                //
//                byteBuffer.clear();
////                byteBuffer.rewind(); //倒带 position = 0 mark 作废
//            }
//        }
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(8005));

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while(true){
            // 未使用 selector
            SocketChannel client = serverSocketChannel.accept();
            int read =0 ;
            while((read =client.read(byteBuffer))!=-1){
                System.out.println(1);
                byteBuffer.rewind();//重定位，不然pos = capacity 就无法继续读取
            }

        }

    }
}
