package online.superh.kafka.nativeclient.consumer.blance;
/*
    当新增或减少消费者时
        1.分区再均衡（重新分配消费者和分区）
        2.consumer.seek()重偏移量开始消费
 */