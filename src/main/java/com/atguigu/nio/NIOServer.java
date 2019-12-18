package com.atguigu.nio;



import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel server = ServerSocketChannel.open();// 初始化ServerSocketChannel
        InetSocketAddress socketAddress = new InetSocketAddress(7000);

        server.socket().bind(socketAddress);//服务器绑定端口7000，

        server.configureBlocking(false);//*-*********************

        Selector selector = Selector.open();// 初始化Selector


        server.register(selector,SelectionKey.OP_ACCEPT);
        selector.keys();

        while(true){
            // 循环select ，实现监听Channel

            if (selector.select(2000)==0) {// ready Channel个数==0 继续监听
                System.out.println("等待1s，未发生事件");
                continue;
            }

            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            System.out.println("待处理事件数目:"+selector.selectedKeys().size());
            while(selectedKeys.hasNext()){
                // 遍历ready 的key，并执行ops对应事件

                SelectionKey selectionKey =  selectedKeys.next();

             if(selectionKey.isAcceptable()){
                    // ready for accepting 则执行accept事件

                    SocketChannel client = server.accept();
                    System.out.println("和一个client建立连接"+client.hashCode());

                    client.configureBlocking(false);//设置 非阻塞才能 registry

                    client.register(selector,SelectionKey.OP_READ,ByteBuffer.allocate(1024));//注册ops = OP_READ ,selector关注读取事件
                    System.out.println("selector.publicKeys.size():"+selector.keys().size());

                }

                if(selectionKey.isReadable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();

                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

                    client.read(byteBuffer);//buffer 读取client.channel的数据

                    System.out.println(new String(byteBuffer.array()));


                }

//                selector.selectedKeys().remove(selectionKey);// selector.remove 执行过的key
                selectedKeys.remove();// iterator.remove() 仅在it.next()方法后执行，去除刚访问过的元素

            }
        }



//        //创建ServerSocketChannel -> ServerSocket
//
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//
//        //得到一个Selecor对象
//        Selector selector = Selector.open();
//
//        //绑定一个端口6666, 在服务器端监听
//        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
//        //设置为非阻塞？
//        serverSocketChannel.configureBlocking(false);
//
//        //把 serverSocketChannel 注册到  selector 关心 事件为 OP_ACCEPT
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//        System.out.println("注册后的selectionkey 数量=" + selector.keys().size()); // 1
//
//
//
//        //循环等待客户端连接
//        while (true) {
//
//            //这里我们等待1秒，如果没有事件发生, 返回
//            if(selector.select(1000) == 0) { //没有事件发生
//                System.out.println("服务器等待了1秒，无连接");
//                continue;
//            }
//
//            //如果返回的>0, 就获取到相关的 selectionKey集合
//            //1.如果返回的>0， 表示已经获取到关注的事件
//            //2. selector.selectedKeys() 返回关注事件的集合
//            //   通过 selectionKeys 反向获取通道
//            Set<SelectionKey> selectionKeys = selector.selectedKeys();
//            System.out.println("selectionKeys 数量 = " + selectionKeys.size());
//
//            //遍历 Set<SelectionKey>, 使用迭代器遍历
//            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
//
//            while (keyIterator.hasNext()) {
//                // 遍历所有发生事件的SelectedKey，根据key 对应的通道发生的事件做相应处理
//                /**
//                 *   SelectedPublicKeys集合 【存储将要发生事件的 SelectKey】
//                 *  【SelectedKey 的事件标志OP ，执行相应事件】
//                 *
//                 */
//                SelectionKey key = keyIterator.next();
//                /**
//                 *  OP_Accept 不为空值，则event = registry
//                 *  执行registry事件
//                 */
//                if(key.isAcceptable()) {
//
//
//                    //该该客户端生成一个 SocketChannel
//                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    System.out.println("客户端连接成功 生成了一个 socketChannel " + socketChannel.hashCode());
//                    //将  SocketChannel 设置为非阻塞
//                    socketChannel.configureBlocking(false);
//                    //将socketChannel 注册到selector, 关注事件为 OP_READ， 同时给socketChannel
//                    //关联一个Buffer
//                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
//
//                    System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size()); //2,3,4..
//
//
//                }
//                /**
//                 *  OP_READ 不为空值，则event = read
//                 *  执行read事件
//                 *
//                 */
//                if(key.isReadable()) {
//
//                    //通过key 反向获取到对应channel
//                    SocketChannel channel = (SocketChannel)key.channel();
//
//                    //获取到该channel关联的buffer
//                    ByteBuffer buffer = (ByteBuffer)key.attachment();
//                    channel.read(buffer);
//                    System.out.println("form 客户端 " + new String(buffer.array()));
//
//                }
//
//                //手动从集合中移动当前的selectionKey, 防止重复操作
//                keyIterator.remove();
//
//            }

        }

    }

