flume-ng-kafka-sink
================

 **ATTENTION** flume-ng-kafka-sink has been merged into flume 1.6 which will release soon. This repo **WILL NOT** in maintenance.

This project is used for [flume-ng](https://github.com/apache/flume) to communicate with [kafka 0.7,2](http://kafka.apache.org/07/quickstart.html).

Configuration of Kafka Sink
----------

    agent_log.sinks.kafka.type = com.vipshop.flume.sink.kafka.KafkaSink
    agent_log.sinks.kafka.channel = all_channel
    agent_log.sinks.kafka.zk.connect = 127.0.0.1:2181
    agent_log.sinks.kafka.topic = all
    agent_log.sinks.kafka.batchsize = 200
    agent_log.sinks.kafka.producer.type = async
    agent_log.sinks.kafka.serializer.class = kafka.serializer.StringEncoder

Speical Thanks
---------

In fact I'm a newbie in Java. I have learnt a lot from [flumg-ng-rabbitmq](https://github.com/jcustenborder/flume-ng-rabbitmq). Thanks to [Jeremy Custenborder](https://github.com/jcustenborder).

