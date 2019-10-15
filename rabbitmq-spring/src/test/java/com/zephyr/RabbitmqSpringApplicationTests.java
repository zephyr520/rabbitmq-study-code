package com.zephyr;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zephyr.entity.Order;
import com.zephyr.entity.Packaged;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Test
	public void testRabbitAdmin() {
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

		rabbitAdmin.declareBinding(new Binding("test.direct.queue", Binding.DestinationType.QUEUE, "test.direct",
				"direct", new HashMap<>()));

		rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.topic.queue", false))
				.to(new TopicExchange("test.topic", false, false))
				.with("user.#"));
		
		rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue("test.fanout.queue", false))
				.to(new FanoutExchange("test.fanout", false, false)));
		
		// 清空队列中的消息
		rabbitAdmin.purgeQueue("dlx.queue", false);
	}
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Test
	public void testSendMessage() throws Exception {
		MessageProperties properties = new MessageProperties();
		properties.getHeaders().put("desc", "信息描述");
		properties.getHeaders().put("type", "自定义消息类型");
		
		Message message = new Message("Hello RabbitMQ".getBytes(), properties);
		
		rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {

			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				System.err.println("------添加额外的配置---------");
				message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
				message.getMessageProperties().getHeaders().put("attr", "添加额外的属性");
				return message;
			}
			
		});
	}
	
	@Test
	public void testSendMessage2() throws Exception {
		MessageProperties properties = new MessageProperties();
		properties.setContentType("text/plain");
		
		Message message = new Message("MQ消息1234".getBytes(), properties);
		
		rabbitTemplate.send("topic001", "spring.abc", message);
		rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello Object message send!");
		rabbitTemplate.convertAndSend("topic002", "rabbit.amqp", "hello Object message send!");
	}
	
	@Test
	public void testSendMessage4Text() throws Exception {
		MessageProperties properties = new MessageProperties();
		properties.setContentType("text/plain");
		
		Message message = new Message("MQ消息1234".getBytes(), properties);
		
		rabbitTemplate.send("topic001", "spring.abc", message);
		rabbitTemplate.send("topic002", "rabbit.abc", message);
	}
	
	@Test
	public void testSendJsonMessage() throws Exception {
		
		Order order = new Order();
		order.setId("001");
		order.setName("消息订单");
		order.setContent("描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.order", message);
	}
	
	@Test
	public void testSendJavaMessage() throws Exception {
		
		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json);
		
		MessageProperties messageProperties = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties.setContentType("application/json");
		messageProperties.getHeaders().put("__TypeId__", "com.zephyr.entity.Order");
		Message message = new Message(json.getBytes(), messageProperties);
		
		rabbitTemplate.send("topic001", "spring.order", message);
	}
	
	@Test
	public void testSendMappingMessage() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		Order order = new Order();
		order.setId("001");
		order.setName("订单消息");
		order.setContent("订单描述信息");
		
		String json1 = mapper.writeValueAsString(order);
		System.err.println("order 4 json: " + json1);
		
		MessageProperties messageProperties1 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties1.setContentType("application/json");
		messageProperties1.getHeaders().put("__TypeId__", "order");
		Message message1 = new Message(json1.getBytes(), messageProperties1);
		rabbitTemplate.send("topic001", "spring.order", message1);
		
		Packaged pack = new Packaged();
		pack.setId("002");
		pack.setName("包裹消息");
		pack.setDescription("包裹描述信息");
		
		String json2 = mapper.writeValueAsString(pack);
		System.err.println("pack 4 json: " + json2);

		MessageProperties messageProperties2 = new MessageProperties();
		//这里注意一定要修改contentType为 application/json
		messageProperties2.setContentType("application/json");
		messageProperties2.getHeaders().put("__TypeId__", "packaged");
		Message message2 = new Message(json2.getBytes(), messageProperties2);
		rabbitTemplate.send("topic001", "spring.pack", message2);
	}
}
