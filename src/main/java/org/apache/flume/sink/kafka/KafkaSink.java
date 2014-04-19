/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.apache.flume.sink.kafka;

import com.google.common.base.Preconditions;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * A Sink of Kafka which get events from channels and publish to Kafka. I use
 * this in our company production environment which can hit 100k messages per
 * second. <tt>zk.connect: </tt> the zookeeper ip kafka use.
 * <p>
 * <tt>topic: </tt> the topic to read from kafka.
 * <p>
 * <tt>batchSize: </tt> send serveral messages in one request to kafka.
 * <p>
 * <tt>producer.type: </tt> type of producer of kafka, async or sync is
 * available.<o> <tt>serializer.class: </tt>{@kafka.serializer.StringEncoder
 * 
 * 
 * }
 */
public class KafkaSink extends AbstractSink implements Configurable {
	private static final Logger log = LoggerFactory.getLogger(KafkaSink.class);
    private Properties parameters;
    private String topic;
	private Producer<String, String> producer;

    private static final String PARTITION_KEY_NAME = "custom.partition.key";
    private static final String CUSTOME_TOPIC_KEY_NAME = "custom.topic.name";
    private static final String DEFAULT_ENCODING = "UTF-8";

	public Status process() throws EventDeliveryException {
        Status status = null;
        Channel channel = getChannel();
		Transaction tx = channel.getTransaction();
		try {
            tx.begin();
            Event event = channel.take();

            if (event != null) {
                String partitionKey = (String) parameters.get(PARTITION_KEY_NAME);
                String topic = Preconditions.checkNotNull((String) this.parameters.get(CUSTOME_TOPIC_KEY_NAME),
                        "topic name is required");
                String eventData = new String(event.getBody(), DEFAULT_ENCODING);
                KeyedMessage<String, String> data = (partitionKey.isEmpty()) ? new KeyedMessage<String, String>(topic,
                        eventData) : new KeyedMessage<String, String>(topic, partitionKey, eventData);
                log.info("Sending Message to Kafka : [" + topic + ":" + eventData + "]");
                producer.send(data);
                tx.commit();
                log.info("Send message success");
                status = Status.READY;
            } else {
                tx.rollback();
                status = Status.BACKOFF;
            }

//			if (event == null) {
//				tx.commit();
//				return Status.READY;
//
//			}
//			producer.send(new ProducerData<String, String>(topic, new String(event
//					.getBody())));
//			log.trace("Message: {}", event.getBody());
//			tx.commit();
//			return Status.READY;
		} catch (Exception e) {
			try {
				tx.rollback();
				status = Status.BACKOFF;
			} catch (Exception e2) {
				log.error("Rollback Exception:{}", e2);
			}		
			log.error("KafkaSink Exception:{}", e);
            status =  Status.BACKOFF;
		} finally {
			tx.close();
		}

        return status;
	}

	public void configure(Context context) {
		topic = context.getString("topic");
		if (topic == null) {
			throw new ConfigurationException("Kafka topic must be specified.");
		}
		producer = KafkaSinkUtil.getProducer(context);
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public synchronized void stop() {
		producer.close();
		super.stop();
	}
}
