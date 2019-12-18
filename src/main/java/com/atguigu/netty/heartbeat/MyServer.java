package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {

        /**
         *      目的： 建立心跳检测机制
         *      IdleStateEvent 发生时，
         *      1. 触发IdleStateHandler(readIdleTime,writeIdleTime,allIdleTime) ，
         *          readIdleTime 读空闲时限
         *          writeIdleTime 写空闲时限
         *          allIdleTime 读写空闲时限
         *      2 .并调用下一个handler（自定义） . userTriggerEvent() 方法处理IdleStateEvent
         * */

        // 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务端启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 配置 ServerBootStrap
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(
                                            new IdleStateHandler(3,5,7));

                                    ch.pipeline().addLast(new MyServerHandler());

                                }
                            }
                    );

            ChannelFuture cf = serverBootstrap.bind(7000).sync();

            cf.channel().closeFuture();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            e.printStackTrace();
        }


//        //创建两个线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
//        try {
//
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup, workerGroup);
//            serverBootstrap.channel(NioServerSocketChannel.class);
//            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//                    //加入一个netty 提供 IdleStateHandler
//                    /*
//                    说明
//                    1. IdleStateHandler 是netty 提供的处理空闲状态的处理器
//                    2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
//                    3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
//                    4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
//
//                    5. 文档说明
//                    triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
// * read, write, or both operation for a while.
// *                  6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个handler去处理
// *                  通过调用(触发)下一个handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读空闲，写空闲，读写空闲)
//                     */
//
//                    //注意：！！
//                    // IdleStateEvent 事件触发后，传递给IdleStateHandler下一个handler.userEventTrigger()方法处理
//                    pipeline.addLast(new IdleStateHandler(7000,7000,10, TimeUnit.SECONDS));
//
//
//                    //加入一个对空闲检测进一步处理的handler(自定义)
//                    pipeline.addLast(new MyServerHandler());
//                }
//            });
//
//            //启动服务器
//            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }
}
