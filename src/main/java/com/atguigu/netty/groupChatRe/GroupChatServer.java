package com.atguigu.netty.groupChatRe;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {

    private int port; //监听端口


    public GroupChatServer(int port) {
        this.port = port;
    }

    //编写run方法，处理客户端的请求
    public void run() throws  Exception{


        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {


            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(bossGroup,workerGroup)// 组合两个线程组
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)// SO_BACKLOG 连接请求最长队列
                    .childOption(ChannelOption.SO_KEEPALIVE,true)// SO_KEEPALIVE 通道一直保持活跃状态
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {

                            // client---> server传输数据，则 ChannelHandlerContext head-->tail
                            ch.pipeline().addLast(new StringDecoder());// 加入解码器

                            ch.pipeline().addLast(new StringEncoder());// 加入编码器
                            /**
                             * pipeline 添加 解码 编码器 的原因：
                             *  MyServerHandler extends SimpleChannelInboundHandler<String>中 msg被解析？
                             */
                            ch.pipeline().addLast(new GroupChatServerHandler());
                        }
                    });// 该handler extends ChannelInitializer


            //启动服务器
            ChannelFuture cf = serverBootstrap.bind(7000).sync();

            // 监听服务器关闭
            cf.channel().closeFuture();
        } catch (InterruptedException e) {
            // 异常发生时，关闭两个线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }


//        //创建两个线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
//
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .option(ChannelOption.SO_BACKLOG, 128)
//                    .childOption(ChannelOption.SO_KEEPALIVE, true)
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//
//                        @Override
//                        protected void initChannel(SocketChannel ch) throws Exception {
//
//                            //获取到pipeline
//                            ChannelPipeline pipeline = ch.pipeline();
//                            //向pipeline加入解码器
//                            pipeline.addLast("decoder", new StringDecoder());
//                            //向pipeline加入编码器
//                            pipeline.addLast("encoder", new StringEncoder());
//                            //加入自己的业务处理handler
//                            pipeline.addLast(new GroupChatServerHandler());
//
//                        }
//                    });
//
//            System.out.println("netty 服务器启动");
//            ChannelFuture channelFuture = b.bind(port).sync();
//
//            //监听关闭
//            channelFuture.channel().closeFuture().sync();
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }

    }

    public static void main(String[] args) throws Exception {

        new GroupChatServer(7000).run();
    }
}
