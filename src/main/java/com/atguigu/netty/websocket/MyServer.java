package com.atguigu.netty.websocket;

import com.atguigu.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.TimeUnit;

public class MyServer {
            //创建两个线程组



//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup, workerGroup);
//            serverBootstrap.channel(NioServerSocketChannel.class);
//            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));// 日志事件发生时，输出Info级别日志
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//                    /**
//                     *      pipeLine 添加Handler
//                     *      1. HttpServerCodec()
//                     *          http解码和编码器---> 数据传输基于http协议
//                     *      2. ChunkedWriteHandler
//                     *          块写方式
//                     *      3. HttpObjectAggregate (int maxObjectLength) // 参数是 整合单元的最大长度
//                     *          http数据是分段传输的，
//                     *          因此浏览器发送大量数据时，发出多个httpRequest
//                     *          通过Aggregate 整合多段数据
//                     *      4. WebSocketServerProtocolHandler("")
//                     *          功能： 将http协议 转换为 ws长连接协议
//                     *          参数webSocketPath ： 明确 localhost:7000/{path} 表示请求的uri
//                     *      5. MyWebSocketFrameHandler 自定义handler
//                     *          实现
//                     */
//                    //因为【基于http协议】，使用http的编码和解码器
//                    pipeline.addLast(new HttpServerCodec());
//
//                    //是以块方式写，添加ChunkedWriteHandler处理器
//                    pipeline.addLast(new ChunkedWriteHandler());
//
//                    /*
//                    说明
//                    1. http数据在【传输过程中是分段】, HttpObjectAggregator ，就是可以将多个段聚合
//                    2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
//                     */
//                    pipeline.addLast(new HttpObjectAggregator(8192));
//                    /*
//                    说明
//                    1. 对应websocket ，它的数据是以 帧(frame) 形式传递
//                    2. 可以看到WebSocketFrame 下面有六个子类
//                    3. 浏览器请求时 ws://localhost:7000/hello 表示请求的uri
//                    4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
//                    5. 是通过一个 状态码 101
//                     */





    public static void main(String[] args) throws Exception{

 //      实现 http协议 -->WebSocket 协议  && TCP短链接--> TCP长连接

        // 1. 创建线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 2. 创建服务端启动器对象

            ServerBootstrap bootstrap = new ServerBootstrap();

            // 3. 配置启动器

            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {// channelInitializer  本质是 ChannelHandler
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();

                                // 1. 添加 http的 编码器 / 解码器
                                //  new HttpServerCodec()
                                pipeline.addLast(new HttpServerCodec());


                                // 2. 添加 块 读取
                                pipeline.addLast(new ChunkedWriteHandler());

                                // 3. http协议 传输大量数据都是分段传输，通过 arregationHandler实现分段数据的整合
                                // new HttpObjectAggregator()
                                pipeline.addLast(new HttpObjectAggregator(8076));

                                // 4. 转换 http协议 --> ws协议
                                // new WebSocketServerPrototypeHandler  即 ws协议handler
                                pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));

                                //5. 处理 从浏览器 client端 读取到数据
                            /**
                             *  http协议规定，传输数据类型 TextWebSocketFrame 为 数据帧，
                             *  通过 HttpServerCodec 编解码器实现数据类型转换
                             *      因此 自定义handler端 读取到的数据类型固定为 TextWebSocketFrame
                             *      handler extends SimpleChannelInboundHandler<TextWebSocketFrame>
                             */

                                pipeline.addLast(new MyTextWebSocketFrameHandler());

                        }

                    });

            // 4. 绑定端口，启动服务器
            ChannelFuture cf = bootstrap.bind(7000).sync();// sync 要求异步操作执行完毕

            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }


//        //创建两个线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup(); //8个NioEventLoop
//        try {
//
//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//            serverBootstrap.group(bossGroup, workerGroup);
//            serverBootstrap.channel(NioServerSocketChannel.class);
//            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));// 日志事件发生时，输出Info级别日志
//            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//
//                @Override
//                protected void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//                    /**
//                     *      pipeLine 添加Handler
//                     *      1. HttpServerCodec()
//                     *          http解码和编码器---> 数据传输基于http协议
//                     *      2. ChunkedWriteHandler
//                     *          块写方式
//                     *      3. HttpObjectAggregate (int maxObjectLength) // 参数是 整合单元的最大长度
//                     *          http数据是分段传输的，
//                     *          因此浏览器发送大量数据时，发出多个httpRequest
//                     *          通过Aggregate 整合多段数据
//                     *      4. WebSocketServerProtocolHandler("")
//                     *          功能： 将http协议 转换为 ws长连接协议
//                     *          参数webSocketPath ： 明确 localhost:7000/{path} 表示请求的uri
//                     *      5. MyWebSocketFrameHandler 自定义handler
//                     *          实现
//                     */
//                    //因为【基于http协议】，使用http的编码和解码器
//                    pipeline.addLast(new HttpServerCodec());
//
//                    //是以块方式写，添加ChunkedWriteHandler处理器
//                    pipeline.addLast(new ChunkedWriteHandler());
//
//                    /*
//                    说明
//                    1. http数据在【传输过程中是分段】, HttpObjectAggregator ，就是可以将多个段聚合
//                    2. 这就就是为什么，当浏览器发送大量数据时，就会发出多次http请求
//                     */
//                    pipeline.addLast(new HttpObjectAggregator(8192));
//                    /*
//                    说明
//                    1. 对应websocket ，它的数据是以 帧(frame) 形式传递
//                    2. 可以看到WebSocketFrame 下面有六个子类
//                    3. 浏览器请求时 ws://localhost:7000/hello 表示请求的uri
//                    4. WebSocketServerProtocolHandler 核心功能是将 http协议升级为 ws协议 , 保持长连接
//                    5. 是通过一个 状态码 101
//                     */
//                    pipeline.addLast(new WebSocketServerProtocolHandler("/hello2"));
//
//                    //自定义的handler ，处理业务逻辑
//                    pipeline.addLast(new MyTextWebSocketFrameHandler());
//                }
//            });
//
//            //启动服务器
//            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
//            channelFuture.channel().closeFuture().sync();
//
//        }finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
    }
}
