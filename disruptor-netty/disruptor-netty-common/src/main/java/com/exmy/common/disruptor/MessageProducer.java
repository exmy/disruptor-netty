package com.exmy.common.disruptor;

import com.exmy.common.entity.TranslatorData;
import com.exmy.common.entity.TranslatorDataWrapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by yiyuan on 2019/1/17 16:40.
 */
public class MessageProducer {

    private String producerId;

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    public MessageProducer(String producerId, RingBuffer<TranslatorDataWrapper> ringBuffer) {
        this.producerId = producerId;
        this.ringBuffer = ringBuffer;
    }

    public void onData(TranslatorData data, ChannelHandlerContext ctx) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWrapper wrapper = ringBuffer.get(sequence);
            wrapper.setData(data);
            wrapper.setCtx(ctx);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
