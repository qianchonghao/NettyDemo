package com.atguigu.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
// 不需要 extends SimpleChannelInboundHandler<>,因为没有read数据

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /**
         *      evt 是 IdleStateEvent
         *      根据event
         */
       if(evt instanceof  IdleStateEvent){
           IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
           IdleState e;// 检测 空闲状态: 读空闲 写空闲 读写空闲
           String msg = "";
            switch ( idleStateEvent.state()){
                case ALL_IDLE:
                    msg= "读写空闲"; break;
                case READER_IDLE:
                    msg= "读空闲"; break;
                case WRITER_IDLE:
                    msg= "写空闲"; break;
            }
           System.out.println(ctx.channel().remoteAddress()+"连接超时，出现："+msg);


       }
    }


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//
//        if(evt instanceof IdleStateEvent) {
//
//            //将  evt 向下转型 IdleStateEvent
//            IdleStateEvent event = (IdleStateEvent) evt;
//            String eventType = null;
//            switch (event.state()) {
//                case READER_IDLE:
//                  eventType = "读空闲";
//                  break;
//                case WRITER_IDLE:
//                    eventType = "写空闲";
//                    break;
//                case ALL_IDLE:
//                    eventType = "读写空闲";
//                    break;
//            }
//            System.out.println(ctx.channel().remoteAddress() + "--超时时间--" + eventType);
//            System.out.println("服务器做相应处理..");
//
//            //如果发生空闲，我们关闭通道
//           // ctx.channel().close();

//    }
}
