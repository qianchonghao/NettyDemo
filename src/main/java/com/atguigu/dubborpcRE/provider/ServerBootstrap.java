package com.atguigu.dubborpcRE.provider;

import com.atguigu.dubborpcRE.netty.NettyServer;//ServerBootstrap作用： 启动nettyServer

public class ServerBootstrap {
    // 启动 NettyServer
    public static void main(String[] args) {
        NettyServer.startServer("localhost",7000);

    }
}
