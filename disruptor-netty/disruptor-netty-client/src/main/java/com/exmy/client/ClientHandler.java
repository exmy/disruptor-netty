package com.exmy.client;

import com.exmy.common.disruptor.MessageProducer;
import com.exmy.common.disruptor.RingBufferWorkerPoolFactory;
import com.exmy.common.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by yiyuan on 2019/1/18 19:06.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData response = (TranslatorData)msg;
        String producerId = "code:sessionId:002";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(response, ctx);
    }
}
