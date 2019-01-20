package com.exmy.common.disruptor;

import com.exmy.common.entity.TranslatorDataWrapper;
import com.lmax.disruptor.WorkHandler;

/**
 * Created by yiyuan on 2019/1/17 16:39.
 */
public class MessageConsumer implements WorkHandler<TranslatorDataWrapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(TranslatorDataWrapper translatorDataWrapper) throws Exception {

    }
}
