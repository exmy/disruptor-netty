package com.exmy.common.disruptor;

import com.exmy.common.entity.TranslatorDataWrapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Created by yiyuan on 2019/1/17 16:40.
 */
public class RingBufferWorkerPoolFactory {
    private static class SingletonHolder {
        static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
    }

    private RingBufferWorkerPoolFactory(){ }

    public static RingBufferWorkerPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private static Map<String, MessageProducer> producers = new ConcurrentHashMap<String, MessageProducer>();

    private static Map<String, MessageConsumer> consumers = new ConcurrentHashMap<String, MessageConsumer>();

    private RingBuffer<TranslatorDataWrapper> ringBuffer;

    private SequenceBarrier sequenceBarrier;

    private WorkerPool<TranslatorDataWrapper> workerPool;

    public void initAndStart(ProducerType type, int bufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        this.ringBuffer = RingBuffer.create(type,
                () -> new TranslatorDataWrapper(),
                bufferSize,
                waitStrategy);
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        this.workerPool = new WorkerPool<TranslatorDataWrapper>(this.ringBuffer,
                this.sequenceBarrier,
                new EventExceptionHandler(), messageConsumers);
        for(MessageConsumer mc : messageConsumers){
            consumers.put(mc.getConsumerId(), mc);
        }
        this.ringBuffer.addGatingSequences(this.workerPool.getWorkerSequences());
        this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2));
    }

    public MessageProducer getMessageProducer(String producerId){
        MessageProducer messageProducer = producers.get(producerId);
        if(null == messageProducer) {
            messageProducer = new MessageProducer(producerId, this.ringBuffer);
            producers.put(producerId, messageProducer);
        }
        return messageProducer;
    }

    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWrapper> {
        public void handleEventException(Throwable ex, long sequence, TranslatorDataWrapper event) {
        }

        public void handleOnStartException(Throwable ex) {
        }

        public void handleOnShutdownException(Throwable ex) {
        }
    }
}
