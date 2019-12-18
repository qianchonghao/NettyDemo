package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    // 存在 编码 解码器， server 读取到的数据类型已经明确
    // 故采用 SimpleChannelHandler<Long>
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务端收到"+ctx.channel().remoteAddress()+"发送的Long信息："+msg);

        ctx.channel().writeAndFlush(987465L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
//
//        System.out.println("从客户端" + ctx.channel().remoteAddress() + " 读取到long " + msg);
//
//        //给客户端发送一个long
//        ctx.writeAndFlush(98765L);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
//        ctx.close();
//    }
}
