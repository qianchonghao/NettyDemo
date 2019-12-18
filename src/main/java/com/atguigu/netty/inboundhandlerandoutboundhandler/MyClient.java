package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
//    public static void main(String[] args)  throws  Exception{
//
//        EventLoopGroup group = new NioEventLoopGroup();
//
//        try {
//
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group).channel(NioSocketChannel.class)
//                    .handler(new MyClientInitializer()); //自定义一个初始化类
//
//            ChannelFuture channelFuture = bootstrap.connect("localhost", 7000).sync();
//
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            group.shutdownGracefully();
//        }
//    }
    public static void main(String[] args) {
        // 1. 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 2. 创建client启动器
            Bootstrap bootstrap = new Bootstrap();

            // 3. 配置bootStrap

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 1.添加解码器
                            pipeline.addLast(new MyByteToLongDecoder());
                            //2.添加编码器
                            pipeline.addLast(new MyLongToByteEncoder());
                            //3.添加自定义handler
                            pipeline.addLast(new MyClientHandler());

                        }
                    });

            // 4. connect 服务端
            ChannelFuture cf = bootstrap.connect("localhost",7000).sync();

            cf.channel().closeFuture();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
            e.printStackTrace();
        }

    }
}
