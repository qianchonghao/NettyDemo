package com.atguigu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class NettyClient {
    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();// 创建线程组对象

        try {


            // 1. 创建客户端启动器
            Bootstrap client = new Bootstrap();

            //2. 配置 客户端启动器
            client.group(group)
                 .channel(NioSocketChannel.class)//    通过反射设置client通道实现类
                 .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    // 泛型参数为什么 设置为 SocketChannel: 因为worker的Loop handler采用NioSocketChannel发送数据
                    // handler 处理 read服务端数据事件
                    ch.pipeline().addLast(new NettyClientHandler());
                }
            });

            //3. 启动客户端
            ChannelFuture cf = client.connect(new InetSocketAddress("localhost",7000)).sync();
            System.out.println("客户端启动");
            cf.channel().closeFuture();




        } catch (InterruptedException e) {
            group.shutdownGracefully();
            e.printStackTrace();
        }
//        //客户端需要一个事件循环组
//        EventLoopGroup group = new NioEventLoopGroup();
//
//
//        try {
//            //创建客户端启动对象
//            //注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
//            Bootstrap bootstrap = new Bootstrap();
//
//            //设置相关参数
//            bootstrap.group(group) //设置线程组
//                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
//                        }
//                    });
//
//            System.out.println("客户端 ok..");
//
//            //启动客户端去连接服务器端
//            //关于 ChannelFuture 要分析，涉及到netty的异步模型
//            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
//            //给关闭通道进行监听
//            channelFuture.channel().closeFuture().sync();
//        }finally {
//
//            group.shutdownGracefully();
//
//        }
    }
}
