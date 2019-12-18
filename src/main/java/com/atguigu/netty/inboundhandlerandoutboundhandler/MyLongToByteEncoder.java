package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    //编码方法
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("encode被调用一次");
        out.writeLong(msg);
        /*
                 acceptOutboundMessage(Object msg) 检测msg是否是应该handler的类型
                 // 【************重点**************】
                 例如此处： Encoder handler的数据类型是Long型，如果传入encode()的msg类型不是Long型，则不进行encode


                public boolean acceptOutboundMessage(Object msg) throws Exception {
                    return matcher.match(msg);
                }
        */

//        System.out.println("MyLongToByteEncoder encode 被调用");
//        System.out.println("msg=" + msg);
//        out.writeLong(msg);

    }
}
