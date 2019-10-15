package com.zephyr.rabbitmq.api.dlx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		String exchangeName = "test_dlx_exchange";
		String routingKey = "dlx.#";
		String queueName = "test_dlx_queue";
		
		channel.exchangeDeclare(exchangeName, "topic", true, false, false, null);
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("x-dead-letter-exchange", "dlx.exchange");
		//这个agruments属性，要设置到声明队列上
		channel.queueDeclare(queueName, true, false, false, arguments);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		// 声明死信队列
		channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
		channel.queueDeclare("dlx.queue", true, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");
		
		// 5、创建消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, 
					BasicProperties properties, byte[] body) throws IOException {
				try {
					System.out.println("consumerTag: " + consumerTag);
					System.out.println("envelope : " + envelope.toString());
					System.out.println("properties : " + properties.toString());
					System.out.println("监听到的消息内容：" + new String(body));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		};
		// 6、设置Channel
		channel.basicConsume(queueName, true, consumer);
		
	}
}
