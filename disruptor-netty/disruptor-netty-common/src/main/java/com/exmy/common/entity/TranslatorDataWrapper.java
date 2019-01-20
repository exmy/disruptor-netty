package com.exmy.common.entity;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by yiyuan on 2019/1/17 16:39.
 */
public class TranslatorDataWrapper {

    private TranslatorData data;

    private ChannelHandlerContext ctx;

    public TranslatorData getData() {
        return data;
    }

    public void setData(TranslatorData data) {
        this.data = data;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

}
