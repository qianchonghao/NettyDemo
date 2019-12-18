package com.atguigu.dubborpcRE.netty;


import com.atguigu.dubborpcRE.customer.ClientBootstrap;
import com.atguigu.dubborpcRE.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//服务器这边handler比较简单
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
        ChannelHandler 目的：  client传输 协议前缀 + args[] ,
            则   return   serverImp.func(args[])
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if(msg.toString().startsWith(ClientBootstrap.providerName)){
                // 符合协议前缀，才能调用 serverImp.func()
                System.out.println("服务端远程调用 HelloServiceImpl，并将结果返回");
                String res = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf('#')+1));

                // 将 serverImp.func(args[])对应res，返回给client端
                ctx.writeAndFlush(res);
            }
    }

    /**
     *   read数据 ，如果所得数据符合 【协议】，
     *       则 ctx.writeAndFlush(new Provider().requestedFunc(receivedParam...)) ;
     */


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //获取客户端发送的消息，并调用服务
//        System.out.println("msg=" + msg);
//
//
//        //定义协议，客户端在调用服务器的api 时，我们需要定义一个协议
//        //比如我们要求 每次发消息是都必须以某个字符串开头 "HelloService#hello#你好"
//        if(msg.toString().startsWith(ClientBootstrap.providerName)) {
//
//            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
//            ctx.writeAndFlush(result);
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
