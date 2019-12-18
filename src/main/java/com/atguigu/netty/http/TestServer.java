package com.atguigu.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) throws Exception {

//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup, workerGroup).
//                    channel(NioServerSocketChannel.class).
//                    childHandler(new TestServerInitializer());
//
//            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
//
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }

        // 创建两个 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 创建服务端 启动器
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 配置bootStrap
        serverBootstrap.group(bossGroup,workerGroup)
                .childHandler(new TestServerInitializer())  //   childHandler(new ChannelInitializer<被监听的Channel类型>)
                .channel(NioServerSocketChannel.class);

        // 绑定端口 启动服务器

        ChannelFuture cf = serverBootstrap.bind(8000).sync();


        //服务器关闭时调用
        cf.channel().closeFuture();

        // 添加listen监听器，监听器采用lambda表达式自定
        cf.addListener(
                future -> {
                    if(future.isSuccess()){// 监听到服务器成功启动
                        System.out.println("服务器启动成功");
                    }else{
                        System.out.println("服务器启动失败");
                    }
                }

        );
    }
}
