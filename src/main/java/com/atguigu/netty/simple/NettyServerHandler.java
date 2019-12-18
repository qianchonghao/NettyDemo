package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/*
说明
1. 我们自定义一个Handler 需要继续netty 规定好的某个HandlerAdapter(规范)
2. 这时我们自定义一个Handler , 才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据实际(这里我们可以读取客户端发送的消息)
    /*
    1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
    2. Object msg: 就是客户端发送的数据 默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /**
         * ChannelHandlerContext 上下文 包含 channel && pipeline
         *  【channelRead目的】： read事件发生后，进行业务处理
         *  【在handler中 write和read的msg 都是 ByteBuf类型】
         *  【eventLoop内部是串行 执行IO操作】，IO操作顺序 = 消息读取+解码+处理（业务可能会长时间阻塞）+编码+发送
         */

        ByteBuf buf = (ByteBuf) msg;// 强转？ Loop调用handler前已经读取数据，并转化为ByteBuf msg

        System.out.println("客户端"+ctx.channel().remoteAddress()+"发送信息："+ buf.toString(CharsetUtil.UTF_8));
        // 注意 buf.toString(CharsetUtil.UTF_8) 设置编码参数

        // eventLoop in worker 自定义任务处理
        // 异步处理 长耗时业务，提交到taskQueue中
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =1 ;i<3;i++){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("等待"+i+"秒");
//                }
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端2",CharsetUtil.UTF_8));
//
//            }
//        });
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =1 ;i<3;i++){
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("等待"+i+"秒");
//                }
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端3",CharsetUtil.UTF_8));
//
//            }
//        });
//        ctx.channel().eventLoop().schedule(new Runnable() {
//            @Override
//            public void run() {
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端4",CharsetUtil.UTF_8));
//            }
//        },3,TimeUnit.SECONDS);


//            //比如这里我们有一个非常耗时长的业务-> 异步执行 -> 提交该channel 对应的
//            //NIOEventLoop 的 taskQueue中,
//
//            //解决方案1 用户程序自定义的普通任务
//
//            ctx.channel().eventLoop().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        Thread.sleep(5 * 1000);
//                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵2", CharsetUtil.UTF_8));
//                        System.out.println("channel code=" + ctx.channel().hashCode());
//                    } catch (Exception ex) {
//                        System.out.println("发生异常" + ex.getMessage());
//                    }
//                }
//            });
//
//            ctx.channel().eventLoop().execute(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        Thread.sleep(5 * 1000);
//                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵3", CharsetUtil.UTF_8));
//                        System.out.println("channel code=" + ctx.channel().hashCode());
//                    } catch (Exception ex) {
//                        System.out.println("发生异常" + ex.getMessage());
//                    }
//                }
//            });
//
//            //解决方案2 : 用户自定义定时任务 -》 该任务是提交到 scheduleTaskQueue中
//
//            ctx.channel().eventLoop().schedule(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        Thread.sleep(5 * 1000);
//                        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵4", CharsetUtil.UTF_8));
//                        System.out.println("channel code=" + ctx.channel().hashCode());
//                    } catch (Exception ex) {
//                        System.out.println("发生异常" + ex.getMessage());
//                    }
//                }
//            }, 5, TimeUnit.SECONDS);



            System.out.println("go on ...");

//        System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channle =" + ctx.channel());
//        System.out.println("server ctx =" + ctx);
//        System.out.println("看看channel 和 pipeline的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
//
//
//        //将 msg 转成一个 ByteBuf
//        //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址:" + channel.remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //
        /*
            1. ctx.channel()是clientChannel，可以直接向clientChannel写入信息
            2. writeAndFlush(Object msg)
                1) 默认参数 msg 为 ByteBuf类型;
                2) Unpooled.copiedBuffer(String msg , 编码) 实现参数输入
                3) 功能： 写入缓存并刷新？？？

         */

            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));

//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵1", CharsetUtil.UTF_8));
    }

    //处理异常, 一般是需要关闭通道

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
