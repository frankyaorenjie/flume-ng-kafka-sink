flume-ng-kafka-sink
================

This project is used for [flume-ng](https://github.com/apache/flume) to communicate with to communicate with Kafka 0.8.1.1 and Scala 2.10.3.


Compile
----------

    mvn package


Installation
----------

Copy the 'flume-ng-kafka-sink-0.8-jar-with-dependencies.jar' to the plugin directory of flume.

Example:

    root@cloud-t1:/opt/apache-flume-1.4.0-bin# tree plugins.d/
    plugins.d/
    └── alex
        └── lib 
            ├── flume-ng-kafka-sink-0.8-jar-with-dependencies.jar
            └── flume-ng-kafka-source-0.8-jar-with-dependencies.jar

Add the directory of plugin to flume-env.sh.

Example:

1. The directory of flume-ng is '/opt/apache-flume-1.4.0-bin'.
2. Makesure a directory named 'plugins.d' in '/opt/apache-flume-1.4.0-bin'.
3. Add the 'FLUME_HOME="/opt/apache-flume-1.4.0-bin"' to '/opt/apache-flume-1.4.0-bin/conf/flume-env.sh'


Configuration of Kafka Sink
----------

	agent_log.sinks.kafka.type = org.apache.flume.sink.kafka.KafkaSink
	agent_log.sinks.kafka.metadata.broker.list = 10.22.203.21:9092,10.22.203.22:9092,10.22.203.23:9092
	agent_log.sinks.kafka.zk.connect = 10.22.203.21:2181,10.22.203.22:2181,10.22.203.23:2181
	agent_log.sinks.kafka.topic = test 
	agent_log.sinks.kafka.batchsize = 200
	agent_log.sinks.kafka.producer.type = async
	agent_log.sinks.kafka.serializer.class = kafka.serializer.StringEncoder
	agent_log.sinks.kafka.channel = mem-channel


Speical Thanks
---------

In fact I'm a newbie in Java. I have learnt a lot from [flumg-ng-rabbitmq](https://github.com/jcustenborder/flume-ng-rabbitmq). Thanks to [Jeremy Custenborder](https://github.com/jcustenborder).

