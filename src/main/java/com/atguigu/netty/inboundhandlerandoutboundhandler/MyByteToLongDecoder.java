package com.atguigu.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *      继承 ByteToMessageDecoder，重写decode方法
     *
     *      1. in.readableBytes();
     *           return 可读取的字节数
     *      2.   out.add(in.readLong());
     *          return 读取到的Long型数据，并将其加入List out
     *      3. 该decoder 被多次调用,直到in 【无数据可读 || 剩余可读字节数<8】 out读取到的内容 传递给下一个inboundHandler中
     *
     *      注意：
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode 被调用");

        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
    /**
     *
     * decode 会根据接收的数据，被调用多次, 直到确定没有新的元素被添加到list
     * , 或者是ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空，就会将list的内容传递给下一个 channelinboundhandler处理, 该处理器的方法也会被调用多次
     *
     * @param ctx 上下文对象
     * @param in 入站的 ByteBuf
     * @param out List 集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
//    @Override
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//
//        System.out.println("MyByteToLongDecoder 被调用");
//        //因为 long 8个字节, 需要判断有8个字节，才能读取一个long
//        if(in.readableBytes() >= 8) {
//            out.add(in.readLong());
//        }
//    }

}
