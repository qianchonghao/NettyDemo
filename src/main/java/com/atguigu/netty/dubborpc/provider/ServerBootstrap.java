package com.atguigu.netty.dubborpc.provider;

import com.atguigu.netty.dubborpc.netty.NettyServer;

//ServerBootstrap作用： 启动nettyServer

public class ServerBootstrap {
    public static void main(String[] args) {

        //代码代填..
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
