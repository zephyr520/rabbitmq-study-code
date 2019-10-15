package com.zephyr.rabbitmq.api.confirm;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
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
		
		// 4、指定消息的投递模式：消息的确认模式
		channel.confirmSelect();
		
		// 5、声明
		String exchangeName = "test_confirm_exchange";
		String routingKey = "confirm.save";
		// 6、通过Channel发送数据
		String msg = "Hello RabbitMQ Send Confirm Message!";
		channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
		
		// 7、添加一个监听
		channel.addConfirmListener(new ConfirmListener() {
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("-------No Ack!---------deliveryTag:" + deliveryTag);
			}
			
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("-------Ack!---------deliveryTag:" + deliveryTag);
			}
		});
		
		// 5、记得要关闭相关连接
//		channel.close();
//		connection.close();
	}
}
