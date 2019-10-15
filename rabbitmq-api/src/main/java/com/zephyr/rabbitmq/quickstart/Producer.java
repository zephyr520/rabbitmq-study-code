package com.zephyr.rabbitmq.quickstart;

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
		// 4、通过Channel发送数据
		for (int i=0; i<5; i++) {
			String msg = "Hello RabbitMQ!";
			channel.basicPublish("", "test001", null, msg.getBytes());
		}
		// 5、记得要关闭相关连接
		channel.close();
		connection.close();
	}
}
