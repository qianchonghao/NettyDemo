package com.atguigu.dubborpcRE.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 *
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context ;
    private String res ;
    private String para;

            // 2. 初始化 msg = 协议前缀 + params
    public void setPara(String param) {
        this.para = param;
    }

    @Override   // 1 初始化 Context
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context= ctx;
    }

    @Override  // 4. 读取 server远程调用的结果 ，赋值给res，并且唤醒call()线程
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        res = msg.toString();
        notify();
    }


    @Override
    // 3. 向server端 传输msg并 【wait() server远程调用结果】
    // 5. res = 远程调用结果 ，读取并return
    public synchronized  Object call() throws Exception {

        context.writeAndFlush(para);

        wait();

        return  res ;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();
    }



//    private ChannelHandlerContext context;//上下文
//    private String result; //返回的结果
//    private String para; //客户端调用方法时，传入的参数
//
//
//    //与服务器的连接创建后，就会被调用, 这个方法是第一个被调用(1)
//
//    /**
//     *  编写ChannelActive目的：
//     *          记录ctx，因为call()需要通过ctx.writeFlush(msg)向server端写入数据
//     * @param ctx
//     */
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println(" channelActive 被调用  ");
//        context = ctx; //因为我们在其它方法会使用到 ctx
//    }
//
//    //收到服务器的数据后，调用方法 (4)
//
//    @Override
//    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(" channelRead 被调用  ");
//        result = msg.toString();
//        notify(); //唤醒等待的线程
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
//    }
//
//    //被代理对象调用, 发送数据给服务器，-> wait -> 等待被唤醒(channelRead) -> 返回结果 (3)-》5
//    @Override
//    public synchronized Object call() throws Exception {
//        System.out.println(" call1 被调用  ");
//        context.writeAndFlush(para);
//        //进行wait
//        wait(); //等待channelRead 方法获取到服务器的结果后，唤醒
//        System.out.println(" call2 被调用  ");
//        return  result; //服务方返回的结果
//    }
//    //(2)
//    void setPara(String para) {
//        System.out.println(" setPara  ");
//        this.para = para;
//    }
}
