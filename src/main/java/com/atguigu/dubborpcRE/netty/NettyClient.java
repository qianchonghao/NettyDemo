package com.atguigu.dubborpcRE.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NettyClient {

//    //创建线程池
//    private static ExecutorService executor =
//            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//    private static NettyClientHandler client;
//    private int count = 0;
//
//    /**
//     * @param serviceClass=被代理类
//     * @param providerName=协议前缀
//     * @return 代理对象
//     */
//
//    public Object getBean(final Class<?> serviceClass, final String providerName) {
//
///**
// *      Proxy.newInstance(ClassLoader , Class<?>[] interfaces,InvocationHandler h)
// *          1. ClassLoader 类加载器
// *          2. interfaces 表示要   代理需要实现的 一组接口
// *          3. InvocationHandler  表示动态代理对象调用方法时，会关联到哪一个InvocationHandler上
// *
// *     InvocationHandler: Proxy 对象需要实现 invocationHandler接口，重写invoke方法
// *      目的： 为了 在被代理对象的方法前后 添加相应处理
// *
// *
// */
//
//        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
//                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
//
//                    /**
//                     *  executor.submit(client);
//                     *      其中client implements Callable,并重写call()方法
//                     *      含义： 通过线程池executor 调用call方法
//                     */
//
//                    System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
//                    //{}  部分的代码，客户端每调用一次 hello, 就会进入到该代码
//
//                    if (client == null) {
//                        initClient();
//                    }
//
//                    //设置要发给服务器端的信息
//                    //providerName 协议头 args[0] 就是客户端调用api hello(???), 参数
//                    client.setPara(providerName + args[0]);
//
//
//                    return executor.submit(client).get();
//
//                });
//    }

    //初始化客户端
//    private static void initClient() {
//        client = new NettyClientHandler();
//        //创建EventLoopGroup
//        NioEventLoopGroup group = new NioEventLoopGroup();
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .handler(
//                        new ChannelInitializer<SocketChannel>() {
//                            @Override
//                            protected void initChannel(SocketChannel ch) throws Exception {
//                                ChannelPipeline pipeline = ch.pipeline();
//                                pipeline.addLast(new StringDecoder());
//                                pipeline.addLast(new StringEncoder());
//                                pipeline.addLast(client);
//                            }
//                        }
//                );
//
//        try {
//            bootstrap.connect("127.0.0.1", 7000).sync();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    /**
     *     1.  ServerBootStrap ： 启动NettyServer，调用NettyServer.start()
     *     2.  ClientBootStrap： 启动NettyClient,调用NettyClient.start()????
     *
     *     3.  NettyServer ：
     *          start() = 线程池初始化 & ServerBootStrap 创建和配置 & 绑定端口并启动
     *     4. NettyServerHandler extends SimpleInboundChannelHandler 【implements Callable】
     *          目的： read Client传输的信息，（1）调用指定接口实现类方法，（2）并返回结果
     *          流程： read0(ChannelPipelineContext ctx,Object msg)
     *                  msg包含 协议前缀 + 远程调用函数 args[]
     *                  协议前缀指定 调用的 接口实现类
     *                  args[] 提供 方法所需的params
     *     5. NettyClient ：
     *          1） init() = 线程池初始化 & BootStrap 创建和配置 & connect()服务端
     *          2） Object getBean(Class serverClass，String provider)   【返回代理类】
     *                   （1） return new Proxy.newInstance(ClassLoader,Class[] interfaces,InvocationHandler);
     *                   （2） InvocationHandler.invoke() 核心方法 ：  在被代理类 业务逻辑方法前后，添加日志管理、数据过滤、权限管理等功能
     *                          a.  clientBootstrap中调用 代理类方法 等价
     *                                  （1）【调用invoke()】+（2）【invoke()通过传递msg=协议前缀+params 到server】+（3）【read server端返回结果】
     *
     *                                  【InvocationHandler内部】：
     *                          b.  clientHandler.setParam(provider+args[])  //设置 被代理类.方法 所需要的参数
     *                  c.  executor.submit(Callable 实现类).get();   // 返回方法调用后的结果，
     *     6. NettyClientHandler
     *          1） synchronized call()   【注意 ： param 和 res 都是ClientHandler的属性，而非局部变量】
     *              1.  write(param)
     *              2.  wait()  // 等待server端调用 实现类方法 并向client传递参数
     *              3.  return result   // 返回远程调用的结果
     *          2） synchronized read(ctx,msg)
     *              1.    res = msg.toString();
     *              2.  notify() 唤醒等待线程
     *
     *          3) ChannelActive(ctx): 设置 handler context上下文变量 ，
     *                  因为call()中 需要通过ctx.write() 传输数据给server
     *
     *
     *
     *
     */
    private static NettyClientHandler clientHandler;

    // 创建线程池
    private static ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static int count =0;
    /**
     *  Client 目的： 返回指定接口 实现类的代理对象
     *
     */
    public Object getBean(Class serverClass,String providerName){
        Class<?>[] classes = {serverClass};
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 每次调用 invoke 方法 ，就是每次远程调用返回结果
                System.out.println("远程调用invoke 方法 次数"+(++count));
                if(clientHandler==null) initClient();
                clientHandler.setPara(providerName+args[0]);
                Future submit = executor.submit(clientHandler);
                return submit.get();// 是否要加 get()

            }
        });
    }
    // 初始化客户端
    private static void initClient(){
        EventLoopGroup group = new NioEventLoopGroup();
        clientHandler = new NettyClientHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();

                                pipeline.addLast(new StringDecoder());
                                pipeline.addLast(new StringEncoder());

                                pipeline.addLast(clientHandler);

                            }
                    });
//.option(ChannelOption.TCP_NODELAY, true)
            ChannelFuture cf = bootstrap.connect("localhost",7000).sync();
            System.out.println("客户端初始化");
            cf.channel().closeFuture();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
            e.printStackTrace();

        }

    }
}

