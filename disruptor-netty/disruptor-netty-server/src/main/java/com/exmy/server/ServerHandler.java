package com.exmy.server;

import com.exmy.common.disruptor.MessageProducer;
import com.exmy.common.disruptor.RingBufferWorkerPoolFactory;
import com.exmy.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by yiyuan on 2019/1/18 18:47.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData request = (TranslatorData)msg;
        String producerId = "code:sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(request, ctx);
    }
}
