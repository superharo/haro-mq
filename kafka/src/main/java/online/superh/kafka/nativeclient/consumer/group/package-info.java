package online.superh.kafka.nativeclient.consumer.group;
/*
    (1)消费者组内每个消费者负责消费不同分区的数据，
    (2)一个分区只能由一个组内消费者消费；
    (3)消费者组之间互不影响。
    (4)所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者
 */