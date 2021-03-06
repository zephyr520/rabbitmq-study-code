package com.zephyr.rabbitmq.api.ack;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Consumer {

	public static void main(String[] args) throws Exception {
		// 1、创建一个ConnectionFactory实例，并配置
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("10.125.28.253");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("zxc");
		connectionFactory.setPassword("zxc123");
		connectionFactory.setVirtualHost("/");
		// 2、通过连接工厂创建一个连接
		Connection connection = connectionFactory.newConnection();
		// 3、通过Connection创建一个Channel
		Channel channel = connection.createChannel();
		// 4、声明(创建)一个队列
		String exchangeName = "test_ack_exchange";
		String routingKey = "ack.#";
		String queueName = "test_ack_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// 5、创建消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, 
					BasicProperties properties, byte[] body) throws IOException {
				try {
					System.out.println("----------------------------");
					System.err.println("consumerTag: " + consumerTag);
					System.err.println("envelope : " + envelope.toString());
					System.err.println("properties : " + properties.toString());
					System.err.println("监听到的消息内容：" + new String(body));
					if ((Integer)properties.getHeaders().get("num") == 0) {
						// 确认失败的消息，设置为重回队列
						channel.basicNack(envelope.getDeliveryTag(), false, true);
					} else {
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		};
		// 6、设置Channel，手工签收，必须要设置autoAck=false
		channel.basicConsume(queueName, false, consumer);
		
	}
}
