package com.zephyr.rabbitmq.api.ack;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

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
		// 4、声明
		String exchangeName = "test_ack_exchange";
		String routingKey = "ack.save";
		// 5、通过Channel发送数据
		Map<String, Object> headers = null;
		AMQP.BasicProperties properties = null;
		for (int i=0; i<5; i++) {
			headers = new HashMap<String, Object>();
			headers.put("num", i);
			
			properties = new AMQP.BasicProperties().builder()
					.deliveryMode(2)
					.contentType("UTF-8")
					.headers(headers)
					.build();
			
			String msg = "Hello RabbitMQ Send Ack Message " + i;
			channel.basicPublish(exchangeName, routingKey, properties, msg.getBytes());
		}
		
		// 6、记得要关闭相关连接
//		channel.close();
//		connection.close();
	}
}
