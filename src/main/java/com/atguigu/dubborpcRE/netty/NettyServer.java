package com.atguigu.dubborpcRE.netty;


import com.sun.security.ntlm.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.StringUtil;

public class NettyServer {


    public static void startServer(String hostName, int port) {
        startServer0(hostName,port);
    }

    //编写一个方法，完成对NettyServer的初始化和启动

    private static void startServer0(String hostname, int port) {
//
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup,workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                                      @Override
//                                      protected void initChannel(SocketChannel ch) throws Exception {
//                                          ChannelPipeline pipeline = ch.pipeline();
//                                          pipeline.addLast(new StringDecoder());
//                                          pipeline.addLast(new StringEncoder());
//                                          pipeline.addLast(new NettyServerHandler()); //业务处理器
//
//                                      }
//                                  }
//
//                    );
//
//            ChannelFuture channelFuture = serverBootstrap.bind(hostname, port).sync();
//            System.out.println("服务提供方开始提供服务~~");
//            channelFuture.channel().closeFuture().sync();
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
        // 创建线程组对象
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            // 创建ServerBootStrap对象

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 配置ServerBootStrap

            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());

                            pipeline.addLast(new NettyServerHandler());

                        }
                    });

            // 启动服务器 绑定端口
            ChannelFuture cf = serverBootstrap.bind(7000).sync();

            System.out.println("服务器已经启动");

            cf.channel().closeFuture();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }


    }
}
