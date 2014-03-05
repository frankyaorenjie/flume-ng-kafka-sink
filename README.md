# flume-ng-kafka-sink


This project is used for [flume-ng](https://github.com/apache/flume) to communicate with [kafka 0.8.0](https://kafka.apache.org/documentation.html#quickstart).

## Configuration of Kafka Sink


    agent_log.sinks.kafka.type = org.apache.flume.sink.kafka.KafkaSink
    agent_log.sinks.kafka.channel = all_channel
    agent_log.sinks.kafka.zk.connect = 127.0.0.1:2181
    agent_log.sinks.kafka.metadata.broker.list = 127.0.0.1:9092
    agent_log.sinks.kafka.topic = all
    agent_log.sinks.kafka.batch.num.messages = 200
    agent_log.sinks.kafka.producer.type = async
    agent_log.sinks.kafka.serializer.class = kafka.serializer.DefaultEncoder
    agent_log.sinks.kafka.request.required.acks = 1

_Supported properties defined by_: [kafka producer configs](https://kafka.apache.org/documentation.html#producerconfigs)

__!! Please replace 127.0.0.1 by an adapted value !!__

## Export all required dependencies

    mvn package dependency:copy-dependencies -DoutputDirectory=target/lib


All the required external dependencies will be in target/lib folder


## Deploy your libs to flume

Copy your artifacts to flume conf directory

    sudo cp -r target/lib /etc/flume-ng/conf/
    sudo cp target/flume-ng-kafka-sink-0.8.0.jar /etc/flume-ng/conf/lib/

Edit your flume-env.sh:

    sudo vim /etc/flume-ng/conf/flume-env.sh
    FLUME_CLASSPATH="lib/*"

And restart your flume agent

    sudo sudo /etc/init.d/flume-ng-agent restart


## Special Thanks

In fact I'm a newbie in Java. I have learnt a lot from [flumg-ng-rabbitmq](https://github.com/jcustenborder/flume-ng-rabbitmq). Thanks to [Jeremy Custenborder](https://github.com/jcustenborder).



