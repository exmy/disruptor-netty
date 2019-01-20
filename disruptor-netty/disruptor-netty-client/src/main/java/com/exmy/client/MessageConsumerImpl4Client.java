package com.exmy.client;

import com.exmy.common.disruptor.MessageConsumer;
import com.exmy.common.entity.TranslatorData;
import com.exmy.common.entity.TranslatorDataWrapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;


/**
 * Created by yiyuan on 2019/1/18 19:07.
 */
public class MessageConsumerImpl4Client extends MessageConsumer {

    public MessageConsumerImpl4Client(String consumerId) {
        super(consumerId);
    }

    public void onEvent(TranslatorDataWrapper event) throws Exception {
        TranslatorData response = event.getData();
        ChannelHandlerContext ctx = event.getCtx();
        try {
            System.err.println("Client: id= " + response.getId()
                    + ", name= " + response.getName()
                    + ", message= " + response.getMessage());
        } finally {
            ReferenceCountUtil.release(response);
        }
    }
}
