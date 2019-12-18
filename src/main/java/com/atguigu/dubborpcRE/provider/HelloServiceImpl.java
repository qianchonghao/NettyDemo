package com.atguigu.dubborpcRE.provider;

import com.atguigu.dubborpcRE.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
    // 编写publicInterface公共接口 实现类
    static int count=0;
    @Override
    public String hello(String msg) {
        if(!msg.isEmpty()){
            return "客户端你好，我收到第"+(++count)+"条信息："+msg;
        }else{
            return "客户端你好，我仅收到空信息";
        }
    }

//    private static int count = 0;
//    //当有消费方调用该方法时， 就返回一个结果
//    @Override
//    public String hello(String mes) {
//        System.out.println("收到客户端消息=" + mes);
//        //根据mes 返回不同的结果
//        if(mes != null) {
//            return "你好客户端, 我已经收到你的消息 [" + mes + "] 第" + (++count) + " 次";
//        } else {
//            return "你好客户端, 我已经收到你的消息 ";
//        }
//    }
}
