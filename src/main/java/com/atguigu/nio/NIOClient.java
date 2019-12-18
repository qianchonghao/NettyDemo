package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel client = SocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost",7000);

        client.configureBlocking(false);


        if(!client.connect(inetSocketAddress)){
            while(!client.finishConnect()){
                System.out.println("在连接完成前，处理其他事件");
            }
        }

        String msg = "hello";

        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        client.write(buffer);
//        System.in.read();// 代码停留此处？
        Thread.sleep(Long.MAX_VALUE);


//        //得到一个网络通道
//        SocketChannel socketChannel = SocketChannel.open();
//        //设置非阻塞
//        socketChannel.configureBlocking(false);
//        //提供服务器端的ip 和 端口
//        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
//        //连接服务器
//        if (!socketChannel.connect(inetSocketAddress)) {
//
//            while (!socketChannel.finishConnect()) {
//                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
//            }
//        }
//
//        //...如果连接成功，就发送数据
//        String str = "hello, 尚硅谷~";
//        //Wraps a byte array into a buffer
//        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
//
//        //wrap 创建合适大小的buffer 并将byte[]写入buffer； 等价下面三行代码
////        ByteBuffer buffer = ByteBuffer.allocate(str.getBytes().length);
////        buffer.put(str.getBytes());
////        buffer.flip();
//
//        //发送数据，将 buffer 数据写入 channel
//        socketChannel.write(buffer);
//        System.in.read();

    }
}
