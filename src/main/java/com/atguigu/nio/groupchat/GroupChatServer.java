
package com.atguigu.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
//    //定义属性
//    private Selector selector;
//    private ServerSocketChannel listenChannel;
//    private static final int PORT = 6667;
//
//    //构造器
//    //初始化工作
//    public GroupChatServer() {
//
//        try {
//
//            //得到选择器
//            selector = Selector.open();
//            //ServerSocketChannel
//            listenChannel =  ServerSocketChannel.open();
//            //绑定端口
//            listenChannel.socket().bind(new InetSocketAddress(PORT));
//            //设置非阻塞模式
//            listenChannel.configureBlocking(false);
//            //将该listenChannel 注册到selector
//            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    //监听
//    public void listen() {
//
//        System.out.println("监听线程: " + Thread.currentThread().getName());
//        try {
//
//            //循环处理
//            while (true) {
//
//                int count = selector.select();
//                if(count > 0) {//有事件处理
//
//                    //遍历得到selectionKey 集合
//                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                    while (iterator.hasNext()) {
//                        //取出selectionkey
//                        SelectionKey key = iterator.next();
//
//                        //监听到accept
//                        if(key.isAcceptable()) {
//                            SocketChannel sc = listenChannel.accept();
//                            sc.configureBlocking(false);
//                            //将该 sc 注册到seletor
//                            sc.register(selector, SelectionKey.OP_READ);
//
//                            //提示
//                            System.out.println(sc.getRemoteAddress() + " 上线 ");
//
//                        }
//                        if(key.isReadable()) { //通道发送read事件，即通道是可读的状态
//                            //处理读 (专门写方法..)
//
//                            readData(key);
//
//                        }
//                        //当前的key 删除，防止重复处理
//                        iterator.remove();
//                    }
//
//                } else {
//                    System.out.println("等待....");
//                }
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//
//        }finally {
//            //发生异常处理....
//
//        }
//    }
//
    //读取客户端消息
//    private void readData(SelectionKey key) {
//
//        //取到关联的channle
//        SocketChannel channel = null;
//
//        try {
//           //得到channel
//            channel = (SocketChannel) key.channel();
//            //创建buffer
//            ByteBuffer buffer = ByteBuffer.allocate(1024);
//
//            int count = channel.read(buffer);
//            //根据count的值做处理
//            if(count > 0) {
//                //把缓存区的数据转成字符串
//                String msg = new String(buffer.array());
//                //输出该消息
//                System.out.println("form 客户端: " + msg);
//
//                //向其它的客户端转发消息(去掉自己), 专门写一个方法来处理
//                sendInfoToOtherClients(msg, channel);
//            }
//
//        }catch (IOException e) {
//            try {
//                System.out.println(channel.getRemoteAddress() + " 离线了..");
//                //取消注册
//                key.cancel();
//                //关闭通道
//                channel.close();
//            }catch (IOException e2) {
//                e2.printStackTrace();;
//            }
//        }
//    }
//
//    //转发消息给其它客户(通道)
//    private void sendInfoToOtherClients(String msg, SocketChannel self ) throws  IOException{
//
//        System.out.println("服务器转发消息中...");
//        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
//        //遍历 所有注册到selector 上的 SocketChannel,并排除 self
//        for(SelectionKey key: selector.keys()) {
//
//            //通过 key  取出对应的 SocketChannel
//            Channel targetChannel = key.channel();
//
//            //排除自己
//            if(targetChannel instanceof  SocketChannel && targetChannel != self) {
//
//                //转型
//                SocketChannel dest = (SocketChannel)targetChannel;
//                //将msg 存储到buffer
//                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
//                //将buffer 的数据写入 通道
//                dest.write(buffer);
//            }
//        }
//
//    }
//public static void main(String[] args) {
//
//    //创建服务器对象
//    GroupChatServer groupChatServer = new GroupChatServer();
//    groupChatServer.listen();
//}
    // 定义server端属性
    ServerSocketChannel server;
    Selector selector ;

    public  GroupChatServer(){// 初始化 server
        try {
            server = ServerSocketChannel.open();
            selector = Selector.open();

            server.socket().bind(new InetSocketAddress(7000));//server绑定端口

            server.configureBlocking(false);//设置非阻塞，使得registry可以进行

            /**
             *  1. 在selector上注册ServerSocketChannel
             *          registry() 将 server's corresponding key 添加到 publicKeys集合中
             *          publicKeys集合存储【**注册过的key**】
             *  2. SelectionKey.OP_ACCEPT 参数： 【**表明selector关注server建立连接的事件**】
             *          通俗讲： socketChannel 请求连接时， selector.select()会将server‘s corresponding SelectionKey 添加到SelectedPublicKeys集合中
             *          SelectedPublicKeys集合存储 【**发生关注事件的key**】
             */
            server.register(selector,SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            System.out.println("服务端异常");
            e.printStackTrace();
        }

    }

    public void  listen(){
        /**
         *  Selector.select()监听事件 ； 事件共两种
         *      1. server 接收到clientSocketChannel 建立连接请求： select()将server’s corresponding key 添加到 SelectedPublicKeys
         *      2. server 读取到clientSocketChannel 数据 ： select() 将 clientChannel's corresponding key 添加到  SelectedPublicKeys
         */
       while(true){// 循环监听
           try {
               if(selector.select(1000)==0){//监听阻塞 1s
                   System.out.println("等待1s，无 server.accept() || read ClientChannel 事件发生 ");
                   continue;
               }
               // 事件发生，遍历Selector.selectedKeys() 处理所有事件
               Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

               while(keyIterator.hasNext()){
                   SelectionKey key = keyIterator.next();
                   if(key.isAcceptable()){
                       // 如果是accept()事件
                       SocketChannel client = server.accept();
                       client.configureBlocking(false);

                       client.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));

                       System.out.println("client["+client.getRemoteAddress()+"]和server建立连接");
                   }
                   if(key.isReadable()){
                       // 如果是read事件

                       // 编写函数， try catch 特殊处理
                       readData(key);
//                       SocketChannel client = (SocketChannel) key.channel();
//
//                       ByteBuffer buffer = (ByteBuffer) key.attachment();//获取 和ClientChannel绑定的 buffer
//
//                       client.read(buffer);
//
//                       String msg = new String(buffer.array());
//
//                       sendInfoToOthers(msg,client); // 将read到信息传输给其他 client
//                       System.out.println(client.getRemoteAddress()+"读取内容："+new String(buffer.array()));

                   }

                   keyIterator.remove();// 从selector.SelectedPublicKeys 去除已处理事件key

               }

           } catch (IOException e) {
               e.printStackTrace();
           }

       }
    }

    private void readData(SelectionKey key) {
        SocketChannel client = null;

        try {

            client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
/**
 *  buffer 不要使用att ，每次新建 buffer 。
 *  否则需要 buffer.clear()
 */
            client.read(buffer);

            String msg = new String(buffer.array());

            sendInfoToOthers(msg,client); // 将read到信息传输给其他 client
        } catch (IOException e) {
            // read || sendToOthers异常 则 cancel key，关闭Client

            try {
                sendInfoToOthers(client.getRemoteAddress()+"离线了",client);
                key.cancel();
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void sendInfoToOthers(String msg, SocketChannel self) throws IOException { // 此处Exception 抛出给readData处理
        // msg传输给所有注册过的client
        Iterator<SelectionKey> keyIterator = selector.keys().iterator();

        while(keyIterator.hasNext()){


                SelectionKey key = keyIterator.next();
                SocketChannel client ;
                if(key.channel() instanceof  SocketChannel && (client = (SocketChannel) key.channel())!=null
                ){
                    // 排除 Server 和 Self，将数据传输给其他CLient
                    client.write(ByteBuffer.wrap(msg.getBytes()));


                }else {
                    continue;
                }

        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }


}

//可以写一个Handler
class MyHandler {
    public void readData() {

    }
    public void sendInfoToOtherClients(){

    }
}

