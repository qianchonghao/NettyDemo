package com.atguigu.netty.tcp;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
    public static void main(String[] args) throws Exception{

        /**
         *  ServerBootstrap 服务器启动器
         *      需要配置ServerBootStrap的相关参数，才能启动服务器
         *      1. group(EventLoopGroup1,EventLoopGroup2)//设置两个线程组
         *      2. channel(NioSocketChannel.class)// 指定Channel类型
         *      3. option(ChannelOption.SO_BACKLOG,127) // 设置线程队列 得到的连接个数
         *      4. childHandler(new MyServerInitializer())
         *          workerGroup的 EventLoop对应channel设置handler
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup).// 两个线程组 组合在一起构成服务端
                    channel(NioServerSocketChannel.class).// 针对Boss使用ServerSocketChannel
                    option(ChannelOption.SO_BACKLOG,128).
                    childOption(ChannelOption.SO_KEEPALIVE,true).// 保持活动连接状态
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        // 针对worker使用SocketChannel
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 创建通道初始化对象

                            ch.pipeline().addLast(null);

                        }
                    });

            // 启动服务器（绑定端口并且同步），生成一个ChannelFuture
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();


            // 对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
