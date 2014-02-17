/*******************************************************************************
 * Copyright 2013 Renjie Yao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.apache.flume.sink.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A Sink of Kafka which get events from channels and publish to Kafka. I use this in our 
 * company production environment which can hit 100k messages per second.
 * <tt>zk.connect: </tt> the zookeeper ip kafka use.<p>
 * <tt>topic: </tt> the topic to read from kafka.<p>
 * <tt>batchSize: </tt> send serveral messages in one request to kafka. <p>
 * <tt>producer.type: </tt> type of producer of kafka, async or sync is available.<o>
 * <tt>serializer.class: </tt>{@kafka.serializer.StringEncoder}
 */
public class KafkaSink extends AbstractSink implements Configurable{
	private static final Logger log = LoggerFactory.getLogger(KafkaSink.class);
	private String topic;
	private Producer<String, String> producer;
	
	public Status process() throws EventDeliveryException {
		Channel channel = getChannel();
		Transaction tx = channel.getTransaction();
		try {
			tx.begin();
			Event e = channel.take();
			if(e == null) {
				tx.rollback();
				return Status.BACKOFF;
			}
			producer.send(new KeyedMessage<String, String>(topic, new String(e.getBody())));
			log.trace("Message: {}", e.getBody());
			tx.commit();
			return Status.READY;
		} catch(Exception e) {
			log.error("KafkaSink Exception:{}",e);
			tx.rollback();
			return Status.BACKOFF;
		} finally {
			tx.close();
		}
	}

	public void configure(Context context) {
		topic = context.getString("topic");
		if(topic == null) {
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
