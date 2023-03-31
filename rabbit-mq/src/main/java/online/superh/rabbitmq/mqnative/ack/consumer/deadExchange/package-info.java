package online.superh.rabbitmq.mqnative.ack.consumer.deadExchange;
/*
    进入死信队列：
    （1）消息 TTL 过期
    （2）队列达到最大长度(队列满了，无法再添加数据到 mq 中)
    （3）消息被拒绝(basic.reject 或 basic.nack)并且 requeue=false.
 */