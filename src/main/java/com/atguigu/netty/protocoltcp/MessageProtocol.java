package com.atguigu.netty.protocoltcp;

/**
 *  出现拆包 || 粘包 的原因：
 *          1. 【read的数据时，没有规定一次读取的数据量】
 *  解决 拆包 粘包问题 ：
 *          1. 定义协议包
 *          2. 添加 MessageToBytesEncoder编码器 & BytesToMessageDecoder 解码器
 *
 *
 *  这么做的原因：
 *      Decoder 限制每次read时 读取到的数据量
 *      Encoder 限制每次write时 写入的数据量
 *
 *
 */
//协议包
public class MessageProtocol {
    private int len; //关键
    private byte[] content;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
