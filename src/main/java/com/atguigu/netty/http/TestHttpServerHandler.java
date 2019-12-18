package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
说明
1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter
2. HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof  HttpRequest){// 检测msg 是否httpRequest



            HttpRequest httpRequest = (HttpRequest)msg ; //强转获取 httpRequest

            URI uri = new URI(httpRequest.uri());// 获取 http请求的uri,过滤指定资源?



            if("/favicon.ico".equals(uri.getPath())) {// ?????????????
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }
            // 检测 ctx所包含的内容
            System.out.println("channel:"+ctx.channel());
            System.out.println("pipeLine:"+ctx.pipeline());
            System.out.println("handler:"+ctx.handler());
            System.out.println("通过Channel获得pipeline:"+ctx.channel().pipeline());// 通过Channel获得pipeline


            /**
             *             1. http不能直接 writeAndFlush(ByteBuf),而是要通过 [**writeAndFlush(HttpResponse response)**]回复信息
             *             2. 通过构造 httpResponse 向client端 回复信息
             *             3. new DefaultHttpResponse(HttpVersion,HttpResponseStatus,ByteBuf)
             */

//            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(..));
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            // 设置 response.headers()
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            ctx.writeAndFlush(response);
        }
    }


    //channelRead0 读取客户端数据
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//
//
////        System.out.println("对应的channel=" + ctx.channel() + " pipeline=" + ctx
////        .pipeline() + " 通过pipeline获取channel" + ctx.pipeline().channel());
////
////        System.out.println("当前ctx的handler=" + ctx.handler());
//
//        //判断 msg 是不是 httprequest请求
//        if(msg instanceof HttpRequest) {
//
//            System.out.println("ctx 类型="+ctx.getClass());
//
//            System.out.println("pipeline hashcode" + ctx.pipeline().hashCode() + " TestHttpServerHandler hash=" + this.hashCode());
//
////            System.out.println("msg 类型=" + msg.getClass());
//            System.out.println("客户端地址" + ctx.channel().remoteAddress());
//
//            //获取到
//            HttpRequest httpRequest = (HttpRequest) msg;
//            //获取uri, 过滤指定的资源
//            URI uri = new URI(httpRequest.uri());
//            if("/favicon.ico".equals(uri.getPath())) {
//                System.out.println("请求了 favicon.ico, 不做响应");
//                return;
//            }
//            //回复信息给浏览器 [http协议]
//
//            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);
//
//            //构造一个http的相应，即 httpresponse
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//
//            //将构建好 response返回
//            ctx.writeAndFlush(response);
//
//        }
//    }



}
