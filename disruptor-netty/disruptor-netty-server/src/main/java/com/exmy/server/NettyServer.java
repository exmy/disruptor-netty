package com.exmy.server;

import com.exmy.common.codec.MarshallingCodeCFactory;
import com.exmy.common.disruptor.RingBufferWorkerPoolFactory;
import com.exmy.common.disruptor.MessageConsumer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * Created by yiyuan on 2019/1/17 16:48.
 */
public class NettyServer {

    public NettyServer() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture cf = serverBootstrap.bind(8765).sync();
            System.err.println("Server Startup...");
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.err.println("Sever ShutDown...");
        }
    }

    public static void main(String[] args) {

        MessageConsumer[] consumers = new MessageConsumer[4];
        for(int i =0; i < consumers.length; i++) {
            MessageConsumer messageConsumer = new MessageConsumerImpl4Server("code:serverId:" + i);
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024*1024,
                //new YieldingWaitStrategy(),
                new BlockingWaitStrategy(),
                consumers);

        new NettyServer();
    }
}
