package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;


public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //initChannel目的:
        //      eventLoop 中pipeLine添加 handler对象， handler对象串行处理


        // 1. 添加http 编解码器 ,【HttpServerCodec是netty提供http编解码器】
        ch.pipeline().addLast("ServerCodec",new HttpServerCodec());
        // 2. 添加自定义handler
        ch.pipeline().addLast("myServerHandler",new TestHttpServerHandler());
    }

//    @Override
//    protected void initChannel(SocketChannel ch) throws Exception {
//
//        //向管道加入处理器
//
//        //得到管道
//        ChannelPipeline pipeline = ch.pipeline();
//
//        //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
//        //HttpServerCodec 说明
//        //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
//        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
//        //2. 增加一个自定义的handler
//        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
//
//        System.out.println("ok~~~~");
//    }
}
