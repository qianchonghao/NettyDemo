package com.atguigu.dubborpcRE.customer;

import com.atguigu.dubborpcRE.netty.NettyClient;
import com.atguigu.dubborpcRE.publicinterface.HelloService;

public class ClientBootstrap {
// clientBootStrap作用： 启动clientServer


    public static final String providerName = "HelloService#hello#"; // 定义协议头，决定调用server端的哪个函数

    public static void main(String[] args) {
        HelloService helloService  = (HelloService) new NettyClient().getBean(HelloService.class,providerName);

        for(;;){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(helloService.hello("远程调用hello"));
        }


    }
//    public static void main(String[] args) throws  Exception{
//
//        //创建一个消费者
//        NettyClient customer = new NettyClient();
//
//        //创建代理对象
//        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);
//
//        for (;; ) {
//            Thread.sleep(2 * 1000);
//            //通过代理对象调用服务提供者的方法(服务)
//            String res = service.hello("你好 dubbo~");
//            System.out.println("调用的结果 res= " + res);
//        }
//    }
}
