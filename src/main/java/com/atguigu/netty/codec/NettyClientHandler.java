package com.atguigu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //连接建立时 client向server发送一个Student 对象到服务器

        /**
         *      目的： 客户端发送 Student对象到 服务器
         *          【利用 protoBuf 替代 netty原生的HttpServerCodec编解码器】
         *      过程：Student.proto  ---通过proto.exe生成----> StudentPOJO.java
         *      本质：【调用 StudentPOJO.Student的 API 创建 StudentPOJO.Student对象】
         *        ~~~~~~~通过API产生的对象，【实现 java-->.proto 的编码】，然后client端 传送proto数据 给服务器
         *      注意： server端接受 proto 数据时，需要解码 ！！
         *
         */
//        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("智多星 吴用").build();
        StudentPOJO.Student student =
                StudentPOJO
                .Student
                .newBuilder()
                .setId(4)   // 设置属性
                .setName("钱崇豪")
                .build();


        //Teacher , Member ,Message
        ctx.writeAndFlush(student);
    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
