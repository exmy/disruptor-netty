package com.exmy.client;

import com.exmy.common.codec.MarshallingCodeCFactory;
import com.exmy.common.disruptor.MessageConsumer;
import com.exmy.common.disruptor.RingBufferWorkerPoolFactory;
import com.exmy.common.entity.TranslatorData;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by yiyuan on 2019/1/18 19:04.
 */
public class NettyClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;

    private Channel channel;

    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture cf;

    public NettyClient() {
        this.connect(HOST, PORT);
    }

    private void connect(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });
            this.cf = bootstrap.connect(host, port).sync();
            System.err.println("Client connected...");

            this.channel = cf.channel();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendData(){

        for(int i = 0; i < 10; i++){
            TranslatorData request = new TranslatorData();
            request.setId("" + i);
            request.setName("请求消息名称 " + i);
            request.setMessage("请求消息内容 " + i);
            this.channel.writeAndFlush(request);
        }
    }

    public void close() throws Exception {
        cf.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
        System.err.println("Sever ShutDown...");
    }

    public static void main(String[] args) {
        MessageConsumer[] consumers = new MessageConsumer[4];
        for(int i =0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumerImpl4Client("code:clientId:" + i);
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024*1024,
                //new YieldingWaitStrategy(),
                new BlockingWaitStrategy(),
                consumers);

        new NettyClient().sendData();
    }
}
