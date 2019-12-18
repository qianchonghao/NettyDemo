package com.atguigu.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

//这里 TextWebSocketFrame 类型，表示一个文本帧(frame)
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        msg.text();
        System.out.println("浏览器发送信息："+ msg.text());

        ctx.channel().writeAndFlush(new TextWebSocketFrame(
                "服务器在"+System.currentTimeMillis()+"时间收到信息"+msg.text()));

    }



    //  ChannelActive 在长连接建立时，触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端和客户端建立连接");
    }

    // 因为长连接，所以理应不会触发 InActive
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端和客户端断开连接");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asLongText()+"调用handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().id().asLongText()+"调用handlerRemoved");
    }

    //    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
//
//        System.out.println("服务器收到消息 " + msg.text());
//
//        //回复消息
//        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
//    }
//
//    //当web客户端连接后， 触发方法
//    @Override
//    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
//        //id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一
//
//        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
//        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
//    }
//
//
//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//
//        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        System.out.println("异常发生 " + cause.getMessage());
//        ctx.close(); //关闭连接
//    }



}
