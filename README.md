flume-ng-kafka-sink
===================

This project is used for [flume-ng 1.4.0](https://github.com/apache/flume) to communicate with [kafka 0.8.1](http://kafka.apache.org/081/quickstart.html).

Configuration of Kafka Sink
------------------------------

    # kafka sink
    producer.sinks.kafka.type = org.apache.flume.sink.kafka.KafkaSink
    producer.sinks.kafka.channel = memChannel
    producer.sinks.kafka.zk.connect = 192.168.86.5:2181
    producer.sinks.kafka.metadata.broker.list = 192.168.86.10:9092
    producer.sinks.kafka.topic = kafkaRocks
    producer.sinks.kafka.serializer.class=kafka.serializer.StringEncoder
    producer.sinks.kafka.request.required.acks=1
    producer.sinks.kafka.max.message.size=1000000

For detailed/full flume sink config, please refer to the sample configuration files in the [samples/flume-conf folder](https://github.com/alanma/flume-ng-kafka-sink/tree/master/samples/flume-conf).

Please also to make sure to indluce needed dependency libs into flume classpath, by default it's in flume/lib. Please refer to the [screenshot here](https://github.com/alanma/flume-ng-kafka-sink/blob/master/samples/flume-lib-dependencies.png) for minimal required dependencies.

###

Special thanks to @baniuyao
---------------------------
For his great flume-ng-kafka plugin, his willingness to share the experience and results with open source community.
