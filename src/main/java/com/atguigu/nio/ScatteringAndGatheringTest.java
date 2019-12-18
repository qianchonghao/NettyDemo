package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;


public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
/**
 *  1. ServerSocketChannel 初始化
 *     1) ServerSocketChannel server = ServerSocketChannel.open();
 *     2) server.socket().bind(new InetSocketAddress(7000)); 绑定端口到socket
 *  2. SocketChannel client = server.accept() 等待Client连接
 *  3. SocketChannel client.read(ByteBuffer[])
 *       buffer数组读取 Channel信息，一个buffer读取满则自动使用下一个buffer
 *
 */
        Selector s;
    ServerSocketChannel server = ServerSocketChannel.open();
    InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

    server.socket().bind(inetSocketAddress);//serverChannel绑定端口7000，并启动

        SocketChannel client = server.accept();//连接Client
        System.out.println("和client建立连接");
        // 初始化 ByteBuffer[]
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]= ByteBuffer.allocate(5);
        byteBuffers[1]= ByteBuffer.allocate(3);

        int dataCount=8;//共读取8字节数据
/**
 * 1. Scattering：利用Buffer[] 实现 Channel --> buffer[]
 *     【buffer[]读取Channel数据】
 *      Buffer[0]-->Buffer[n] 依次从Channel读取数据
 * 2. Gathering:  利用Buffer[] 实现 buffer[] --> Channel
 *     【buffer[]所有数据写入Channel】
 *     Buffer[0]--->Buffer[1] 数据依次写入Channel
 */
    while(true){
        int read = 0;
        while(read<dataCount){//利用buffer[] 读取ClientChannel数据
            int tempCount = (int) client.read(byteBuffers);
            read+= tempCount;

            Arrays.asList(byteBuffers).stream().map(
                    buffer->"postion:"+buffer.position()+"+Limit:"+buffer.limit()
            ).forEach(System.out::println);
            /*
            stream().map(链表元素-->映射成链表元素局部变量)
                    .foreach(链表元素[map映射所得元素]--> 链表元素的方法);

             */

        }

        // 所有buffer 读写切换
        Arrays.asList(byteBuffers).stream().forEach(buffer->buffer.flip() );

        int write=0;
        while(write<dataCount){
            int tempCount = (int) client.write(byteBuffers);
            write+=tempCount;
        }

        //所有buffer.clear() 用于读取下次
        Arrays.asList(byteBuffers).stream().forEach(buffer->buffer.clear());
        System.out.println("write:"+write+"read"+read);
    }
//        //使用 ServerSocketChannel 和 SocketChannel 网络
//
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
//
//        //绑定端口到socket ，并启动
//        serverSocketChannel.socket().bind(inetSocketAddress);
//
//        //创建buffer数组
//        ByteBuffer[] byteBuffers = new ByteBuffer[2];
//        byteBuffers[0] = ByteBuffer.allocate(5);
//        byteBuffers[1] = ByteBuffer.allocate(3);
//
//        //等客户端连接(telnet)
//        SocketChannel socketChannel = serverSocketChannel.accept();
//        int messageLength = 8;   //假定从客户端接收8个字节
//        //循环的读取
//        while (true) {
//
//            int byteRead = 0;
//
//            while (byteRead < messageLength ) {
//
//                long l = socketChannel.read(byteBuffers);
//                byteRead += l; //累计读取的字节数
//                System.out.println("byteRead=" + byteRead);
//
//                //使用流打印, 看看当前的这个buffer的position 和 limit
//                Arrays.asList(byteBuffers).stream().map(
//                        buffer -> "postion=" + buffer.position() +
//                                ", limit=" + buffer.limit()).forEach(System.out::println);
//            }
//
//            //将所有的buffer进行flip
//            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());
//
//            long byteWirte = 0;
//            //将数据读出显示到客户端
//            while (byteWirte < messageLength) {
//                long l = socketChannel.write(byteBuffers);
//                byteWirte += l;
//            }
//
//            //将所有的buffer 进行clear
//            Arrays.asList(byteBuffers).forEach(buffer-> {
//                buffer.clear();
//            });
//
//            System.out.println("byteRead:=" + byteRead +
//                    " byteWrite=" + byteWirte + ", messagelength" + messageLength);
//        }
    }
}
