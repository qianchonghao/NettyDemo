package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *      目的：Server 和 Client 通信 Long型数据
 *      核心： 【自定义编码、解码器】
 *
 */
public class MyServer {
    public static void main(String[] args) throws Exception{

//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            serverBootstrap.group(bossGroup,workerGroup).
//                    channel(NioServerSocketChannel.class).
//
//                    childHandler(new MyServerInitializer()); //自定义一个初始化类
//
//
//            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
        // 1. 创建两个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 2. 创建server 启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 3. 配置server启动器

            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    /**
                                     * 【关键点1】
                                     *  pipeLine区分 inboundHandler 和 outboundHandler
                                     *  入栈时 head-->tail 顺序 调用 inboundHandler
                                     *  出站时 tail-->head 顺序 调用 OutboundHandler
                                     *
                                     *  【关键点2】
                                     *  MessageToBytesEncoder 中 acceptOutboundMessage(Object msg)
                                     *      return boolean 数据 ，判断Object msg 是否是需要handler的对象，如果不是则不进行编码
                                     */

                                    // 1. 添加 【属于 InboundHandler 】的 解码器
                                    pipeline.addLast(new MyByteToLongDecoder());

                                    // 2. 添加 【属于 OutboundHandler】的 编码器
                                    pipeline.addLast(new MyLongToByteEncoder());

                                    // 3. 添加自定义handler，实现read和write long型数据
                                    pipeline.addLast(new MyServerHandler());
                                }
                            }
                    );

            // 4. 绑定端口 ，启动服务器

            ChannelFuture cf = serverBootstrap.bind(7000).sync();// 等待异步操作执行完毕

            // 监听 通道关闭
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }


    }
}
