package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class NettyServer {
    public static void main(String[] args) throws Exception {


/**
 *  1. 服务器端 两个线程组 组成： BossGroup + WorkerGroup
 *  2. BossGroup ：轮询accept事件
 *  3. WorkerGroup  ： 轮询IO业务事件
 *  4. Boss || worker 两个线程组的 默认线程数（EventLoop数量） = 2*cpu核心数
 *  5. EventLoopGroup bossGroup = new NioEventLoopGroup(int 线程数);
 *  boss中某个select轮循到Accept事件，则建立连接，创建NioSocketChannel，
 *      并在worker中某一个eventLoop的selector中注册该channel【在0-max序号的eventLoop循环分配】
 *  eventLoop in worker 轮询IO事件，IO事件发生时，创建handler对象处理IO
 *      handler对象通过线程池分配线程处理对应Channel的IO等业务操作
 *
 *
 */


        /**
         *      1. 创建 服务端 启动器bootStrap
         *      2. 配置bootStrap参数
         *         1） group()    //设置boss && worker线程组
         *         2）channel()
         *               设置服务器端 channel的类型 为 NioServerSocketChannel.class
         *               【**bossGroup的Channel仅在selector上处理 注册为 ACCEPT事件**】
         *         3）option(ChannelOption.BACKLOG,128 )
         *               bootStrap.options 是map结构，存放服务器的属性？
         *
         *         4）childOption(ChannelOption.ACTIVE,true)
         *
         *         5）childHandler(new ChannelInitializer<NioSocketChannel>)
         *              注意：
         *              【**ChannelInitializer<T>d的泛型参数是 ！！客户端的Channel！！类型**】
         *              BossGroup 分发 【 **该Client.Channel**】 给 workerGroup某一个EventLoop.Selector进行监听
         *              pipeLine 和 channel 相互包含，二者是同一级别
         *
         *              ！！！！ ChannelHandler ！！！！！
         *              1. 【initChannel目的】：【**在pipeline中 添加handler对象**】
         *              2. ChannelHandler 类型：
         *                  - ChannelInboundHandler 入栈 ，处理read事件
         *                  - ChannelOutboundHandler 出栈 处理write事件
         *              3. Loop监听到的事件依次经过pipeline中每个handler处理
         *
         *              ！！！！ChannelFuture！！！！
         *              Netty中IO操作都是异步。如：
         *                  ChannelFuture cf = serverBootStrap.bind(int port).sync();
         *                  ChannelFuture cf = bootStrap.connect(hostName,port).sync();// sync()等待异步操作执行完毕
         *                  异步：
         *                      【ChannelFuture对象在创建时，IO操作未结束】
         *                      通过ChannelFuture.listen(new Runnable) 监听IO操作，并在IO完毕后执行添加操作
         *
         *             ！！！！ChannelPipeline ！！！！
         *             1. pipeline等价 List<ChannelHandler>,用于拦截Channel的入栈和出栈操作
         *             2. pipeLine.addLast(new ChannelHandler); 向pipeLine中添加 handler
         *
         *
         *         6）handler(null)
         *              该 handler对应 bossGroup , childHandler 对应 workerGroup
         *              【handler 事件发生时处理事件】
         *      3. bootStrap.bind(int port).sync(); 启动服务器【即绑定端口】，然后同步
         */


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();//1. 创建 服务端 启动器bootStrap

            // 2. 配置bootStrap参数
            serverBootstrap.group(bossGroup,workerGroup).
                    channel(NioServerSocketChannel.class).
                    option(ChannelOption.SO_BACKLOG,128).
                    childOption(ChannelOption.SO_KEEPALIVE,true).
//                    handler(null).
                    childHandler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            System.out.println("客户socketchannel hashcode=" + ch.hashCode());

                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            // 3. bootStrap.bind(int port).sync(); 启动服务器【即绑定端口】，然后同步
            ChannelFuture cf = serverBootstrap.bind(7000).sync();
            System.out.println("客户端启动");
            cf.addListener(new ChannelFutureListener() {//
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口 6668 成功");
                    } else {
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });

            cf.channel().closeFuture();//对关闭通道进行监听
        } catch (InterruptedException e) {
            // 异常时关闭 两个线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }

//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//
//
//        try {
//            //创建服务器端的启动对象，配置参数
//            ServerBootstrap bootstrap = new ServerBootstrap();
//
//            //使用链式编程来进行设置
//            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
//                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
//                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数
//                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
////                    .handler(null) // 该 handler对应 bossGroup , childHandler 对应 workerGroup
//                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象(匿名对象)
//                        //给pipeline 设置处理器
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//                            System.out.println("客户socketchannel hashcode=" + ch.hashCode());
//                            //可以使用一个集合管理 SocketChannel， 再推送消息时，
//                            // 可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
//                            ch.pipeline().addLast(new NettyServerHandler());
////                        }
//                        }
//                    }); // 给我们的workerGroup 的 EventLoop 对应的管道设置处理器
//
//            System.out.println(".....服务器 is ready...");
//
//            //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
//            //启动服务器(并绑定端口)
//            final ChannelFuture cf = bootstrap.bind(7000).sync();
//
//            //给cf 注册监听器，监控我们关心的事件
//
//            cf.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (cf.isSuccess()) {
//                        System.out.println("监听端口 6668 成功");
//                    } else {
//                        System.out.println("监听端口 6668 失败");
//                    }
//                }
//            });
//
//
//            //对关闭通道进行监听
//            cf.channel().closeFuture().sync();
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }

    }

}
