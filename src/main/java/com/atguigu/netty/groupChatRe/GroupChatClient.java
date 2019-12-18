package com.atguigu.netty.groupChatRe;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;


public class GroupChatClient {

    //属性
    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     *  client 功能：
     *  1. 向server发送信息
     *  2. 接受server转发的信息
     *
     */
    public void run() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();// 创建线程组

        try {
            Bootstrap bootstrap = new Bootstrap();// 创建客户端启动对象

            bootstrap.group(group)// 配置客户端启动器
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {

                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder());// 解码
                            ch.pipeline().addLast(new StringEncoder());// 编码
                            ch.pipeline().addLast(new GroupChatClientHandler());

                        }
                    });

            ChannelFuture cf =  bootstrap.connect(host,port).sync();// 先完成异步执行
            System.out.println(cf.channel().localAddress()+"和服务器建立连接");

            Scanner scanner = new Scanner(System.in);

            // 接受server发送的数据

            while(scanner.hasNextLine()){
                cf.channel().writeAndFlush(scanner.nextLine()+"\r\n");

            }

            cf.channel().closeFuture();
        } catch (Exception e) {
            group.shutdownGracefully();
            e.printStackTrace();
        }


//        EventLoopGroup group = new NioEventLoopGroup();
//
//        try {
//
//
//        Bootstrap bootstrap = new Bootstrap()
//                .group(group)
//                .channel(NioSocketChannel.class)
//                .handler(new ChannelInitializer<SocketChannel>() {
//
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//
//                        //得到pipeline
//                        ChannelPipeline pipeline = ch.pipeline();
//                        //加入相关handler
//                        pipeline.addLast("decoder", new StringDecoder());
//                        pipeline.addLast("encoder", new StringEncoder());
//                        //加入自定义的handler
//                        pipeline.addLast(new GroupChatClientHandler());
//                    }
//                });
//
//        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
//        //得到channel
//            Channel channel = channelFuture.channel();
//            System.out.println("-------" + channel.localAddress()+ "--------");
//            //客户端需要输入信息，创建一个扫描器
//            Scanner scanner = new Scanner(System.in);
//            while (scanner.hasNextLine()) {
//                String msg = scanner.nextLine();
//                //通过channel 发送到服务器端
//                channel.writeAndFlush(msg + "\r\n");
//            }
//        }finally {
//            group.shutdownGracefully();
//        }
    }

    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
