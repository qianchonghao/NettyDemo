package com.atguigu.bio;



import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BIOServer {
    public static void main(String[] args) throws Exception {

        //线程池机制

        //思路
        //1. 创建一个线程池
        //2. 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)

//        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
//
//        //创建ServerSocket
//        ServerSocket serverSocket = new ServerSocket(6666);
//
//        System.out.println("服务器启动了");
//
//        while (true) {
//
//            System.out.println("线程信息 id =" + Thread.currentThread().getId() +
//                    " 名字=" + Thread.currentThread().getName());
//            //监听，等待客户端连接
//            System.out.println("等待连接....");
//            final Socket socket = serverSocket.accept();//与客户端建立连接
//
//            System.out.println("连接到一个客户端");
//
//            //就创建一个线程，与之通讯(单独写一个方法)
//            newCachedThreadPool.execute(new Runnable() {
//                public void run() { //我们重写
//                    //可以和客户端通讯
//                    handler(socket);
//                }
//            });
//
//        }

        /**
         * 利用 ServerExecutor 线程池 【实现server创建多线程 处理多个client连接请求】
         *
         */
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();//建立线程池

        ServerSocket server = new ServerSocket(6666);//启动服务器
        System.out.println("启动服务器");
        while(true){
            System.out.println("等待client连接");
            final Socket client = server.accept();//与客户端建立连接
            System.out.println("和一个client建立连接");
            /**
             *      通过 cachedThreadPool调用线程 处理每一个Client连接
             *      cachedThreadPool.execute(Runnable ) ThreadPoolExecutor处理client
             *
             */
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                        handler(client);
                }
            });
        }

    }

    //编写一个handler方法，和客户端通讯
    public static void handler(Socket client)  {

//        try {
//            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
//            byte[] bytes = new byte[1024];
//            //通过socket 获取输入流
//            InputStream inputStream = client.getInputStream();
//
//            //循环的读取客户端发送的数据
//            while (true) {
//
//                System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
//
//                System.out.println("read....");
//               int read =  inputStream.read(bytes);
//               if(read != -1) {
//                   System.out.println(new String(bytes, 0, read
//                   )); //输出客户端发送的数据
//               } else {
//                   break;
//               }
//            }
//
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            System.out.println("关闭和client的连接");
//            try {
//                client.close();
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }




        /**
         * 仅实现循环读取 client数据，并未实现返回值给client
         */
        try {
            InputStream is = null;//read client输入数据
            is = client.getInputStream();

            byte[] data = new byte[1024];

            while(true){
                System.out.println("正在read数据");
                int read = is.read(data);
                // read未读取到 数据时，【阻塞在此处】
                System.out.println("读取到数据");
                if(read==-1 ) break;
                System.out.println(Thread.currentThread().getName()+":"+new String(data,0,read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
