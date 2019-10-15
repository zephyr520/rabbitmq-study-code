package com.zephyr.rabbitmq.api.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4TopicExchange {

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
		// 4、声明Exchange和Routingkey
		String exchangeName = "test_topic_exchange";
		String routingKey1 = "user.save";
		String routingKey2 = "user.update";
		String routingKey3 = "user.delete.abc";
		// 5、通过Channel发送数据
		String msg = "Hello World RabbitMQ for Topic Exchange Message ...";
		channel.basicPublish(exchangeName, routingKey1, null, msg.getBytes());
		channel.basicPublish(exchangeName, routingKey2, null, msg.getBytes());
		channel.basicPublish(exchangeName, routingKey3, null, msg.getBytes());
		// 6、记得要关闭相关连接
		channel.close();
		connection.close();
	}

}
