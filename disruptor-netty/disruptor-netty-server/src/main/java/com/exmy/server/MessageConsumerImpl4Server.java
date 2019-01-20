package com.exmy.server;

import com.exmy.common.disruptor.MessageConsumer;
import com.exmy.common.entity.TranslatorData;
import com.exmy.common.entity.TranslatorDataWrapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by yiyuan on 2019/1/18 23:52.
 */
public class MessageConsumerImpl4Server extends MessageConsumer {

    public MessageConsumerImpl4Server(String consumerId) {
        super(consumerId);
    }

    public void onEvent(TranslatorDataWrapper event) throws Exception {
        TranslatorData request = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        System.err.println("Sever端: id= " + request.getId()
                + ", name= " + request.getName()
                + ", message= " + request.getMessage());

        TranslatorData response = new TranslatorData();
        response.setId("resp: " + request.getId());
        response.setName("resp: " + request.getName());
        response.setMessage("resp: " + request.getMessage());

        ctx.writeAndFlush(response);
    }

}
