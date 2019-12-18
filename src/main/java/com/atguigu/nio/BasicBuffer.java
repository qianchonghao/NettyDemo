package com.atguigu.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
/**
 * 一、intBuffer 常用API
 *  1. intBuffer.put(int data);
 *      存放data ， 【buffer内部position++】
 *  2. intBuffer.get() 默认返回position 位置的值。 需要两个前置步骤
 *      1）intBuffer.flip()切换读写，
 *          【实际上就是更新limit为最后位置pos，然后pos指针重新指向0位置】
 *      2）intBuffer.position(int newP)
 *          设置position=newP
 *  3. IntBuffer.allocate(int capacity); // 返回Buffer对象
 *  4. intBuffer.limit(int newL) // limit = newL
 * 二、 intBuffer关键参数
 *      1. capacity //总容量
 *      2. position //当前位置
 *      3. limit //读写最大范围+1 limit处不能读写
 */

        //创建一个Buffer, 大小为 5, 即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);


        for(int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put( i * 2);
        }

        //如何从buffer读取数据
        //将buffer转换，读写切换(!!!)
        /*
        public final Buffer flip() {
        limit = position; //读数据不能超过5
        position = 0;
        mark = -1;
        return this;
    }
         */
        intBuffer.flip();
        intBuffer.position(1);//1,2
        System.out.println(intBuffer.get());
        intBuffer.limit(3);
        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
