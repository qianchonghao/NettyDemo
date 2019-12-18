package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理read事件
        ByteBuf byteBuf = (ByteBuf)msg;

        System.out.println("服务器"+ctx.channel().remoteAddress()+"发送信息： "+byteBuf.toString(CharsetUtil.UTF_8));

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 连接到服务端时 发送信息给服务端
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("服务端你好", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
    //    //当通道就绪就会触发该方法
//    // bootStrap.connect() 触发？
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("client " + ctx);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
//    }
//
//    //当通道有读取事件时，会触发
//    // 即 ServerHandler.
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
}
