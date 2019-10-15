package com.zephyr.rabbitmq.api.returnlistener;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.client.AMQP.BasicProperties;

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
		String exchangeName = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyErr = "abc.save";

		// 7、添加一个监听
		channel.addReturnListener(new ReturnListener() {

			@Override
			public void handleReturn(int replyCode, String replyText, 
					String exchange, String routingKey,
					BasicProperties properties, byte[] body) throws IOException {
				System.out.println("------------ handle return -------------- ");
				System.out.println("replyCode : " + replyCode);
				System.out.println("replyText : " + replyText);
				System.out.println("exchange : " + exchange);
				System.out.println("routingKey : " + routingKey);
				System.out.println("properties : " + properties.toString());
				System.out.println("body : " + new String(body));
			}
		});
		
		// 6、通过Channel发送数据
		String msg = "Hello RabbitMQ Send Return Message!";
//		channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
		channel.basicPublish(exchangeName, routingKeyErr, true, null, msg.getBytes());

		// 5、记得要关闭相关连接
//		channel.close();
//		connection.close();
	}
}
