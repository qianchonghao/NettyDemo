package com.atguigu.netty.groupChatRe;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupChatClientHandler extends SimpleChannelInboundHandler<String> {
    // read server端转发的数据
    // extends SimpleChannelInboundHandler<T> 【***T表示入栈时，读取到的数据类型***】

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("接收到信息："+msg);
    }


//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println(msg.trim());
//    }
}
