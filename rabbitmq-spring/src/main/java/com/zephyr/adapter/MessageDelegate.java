package com.zephyr.adapter;

import java.util.Map;

import com.zephyr.entity.Order;
import com.zephyr.entity.Packaged;

public class MessageDelegate {

	public void handleMessage(byte[] messageBody) {
		System.err.println("默认方法，消息的内容是：" + new String(messageBody));
	}

	public void consumeMessage(byte[] messageBody) {
		System.err.println("自定义方法，消息的内容是：" + new String(messageBody));
	}

	public void consumeMessage(String messageBody) {
		System.err.println("自定义方法，通过转换器，将字节数组的消息转成字符串接收，消息的内容是：" + new String(messageBody));
	}

	public void method1(String messageBody) {
		System.err.println("method1 收到消息内容:" + new String(messageBody));
	}

	public void method2(String messageBody) {
		System.err.println("method2 收到消息内容:" + new String(messageBody));
	}

	public void consumeMessage(Map messageBody) {
		System.err.println("自定义方法，通过转换器，将字节数组的消息转成JSON格式接收，消息的内容是：" + messageBody);
	}
	
	public void consumeMessage(Order order) {
		System.err.println("Order 对象，消息的内容：" + 
				String.format("id:%s, name:%s, content:%s", 
						order.getId(), 
						order.getName(), 
						order.getContent()));
	}
	
	public void consumeMessage(Packaged pack) {
		System.err.println("Packaged 对象，消息的内容：" + 
				String.format("id:%s, name:%s, desc:%s", 
						pack.getId(), 
						pack.getName(), 
						pack.getDescription()));
	}
}
