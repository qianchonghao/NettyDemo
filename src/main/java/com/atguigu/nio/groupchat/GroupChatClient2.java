package com.atguigu.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient2 {

//    //定义相关的属性
//    private final String HOST = "127.0.0.1"; // 服务器的ip
//    private final int PORT = 6667; //服务器端口
//    private Selector selector;
//    private SocketChannel socketChannel;
//    private String username;
//
//    //构造器, 完成初始化工作
//    public GroupChatClient() throws IOException {
//
//        selector = Selector.open();// 设置该Selector 是为了监听该 ClientChannel的read行为
//        //连接服务器
//        socketChannel = socketChannel.open(new InetSocketAddress("127.0.0.1", PORT));
//        //设置非阻塞
//        socketChannel.configureBlocking(false);
//        //将channel 注册到selector
//        socketChannel.register(selector, SelectionKey.OP_READ);
//        //得到username
//        username = socketChannel.getLocalAddress().toString().substring(1);
//        System.out.println(username + " is ok...");
//
//    }
//
//    //向服务器发送消息
//    public void sendInfo(String info) {
//
//        info = username + " 说：" + info;
//
//        try {
//            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //读取从服务器端回复的消息
//    public void readInfo() {
//
//        try {
//
//            int readChannels = selector.select();
//            if(readChannels > 0) {//有可以用的通道
//
//                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                while (iterator.hasNext()) {
//
//                    SelectionKey key = iterator.next();
//                    if(key.isReadable()) {
//                        //得到相关的通道
//                       SocketChannel sc = (SocketChannel) key.channel();
//                       //得到一个Buffer
//                        ByteBuffer buffer = ByteBuffer.allocate(1024);
//                        //读取
//                        sc.read(buffer);
//                        //把读到的缓冲区的数据转成字符串
//                        String msg = new String(buffer.array());
//                        System.out.println(msg.trim());
//                    }
//                }
//                iterator.remove(); //删除当前的selectionKey, 防止重复操作
//            } else {
//                //System.out.println("没有可以用的通道...");
//
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        //启动我们客户端
//        GroupChatClient chatClient = new GroupChatClient();
//
//        //启动一个线程, 每个3秒，读取从服务器发送数据
//        new Thread() {
//            public void run() {
//
//                while (true) {
//                    chatClient.readInfo();
//                    try {
//                        Thread.currentThread().sleep(3000);
//                    }catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//
//        //发送数据给服务器端
//        Scanner scanner = new Scanner(System.in);
//
//        while (scanner.hasNextLine()) {
//            String s = scanner.nextLine();
//            chatClient.sendInfo(s);
//        }
//    }
    // 定义field





    Selector selector ;
    SocketChannel client ;
    String userName;
    public GroupChatClient2(){
        try {
            selector = Selector.open();
            client = SocketChannel.open(new InetSocketAddress("localhost",7000));

            client.configureBlocking(false);

            client.register(selector,SelectionKey.OP_READ);// client在selector上注册，selector关注当前client的read事件


            userName = client.getLocalAddress().toString().substring(1);
            System.out.println(userName + "连接server成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *     在client中 sendInfo 和 readInfo 是两个线程并行执行的
     *
     */
    private void sendInfo(){
        Scanner scanner = new Scanner(System.in);
        String s="";
        while(scanner.hasNextLine()){

            String msg =userName+ "发送信息："+scanner.nextLine();
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
            try {
                client.write(byteBuffer);
                s = new String(byteBuffer.array());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void readInfo(){
        while(true){

            try {
                if(selector.select(1000)==0){
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while(keyIterator.hasNext()){
                    SelectionKey selectionKey = keyIterator.next();

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    if(selectionKey.isReadable()){
                        socketChannel.read(byteBuffer);
                        String s = new String(byteBuffer.array());
                        System.out.println(s);
                    }


                    keyIterator.remove();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public static void main(String[] args) {
        GroupChatClient2 groupChatClient= new GroupChatClient2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                groupChatClient.readInfo();
            }
        }).start();

        groupChatClient.sendInfo();
    }




}
