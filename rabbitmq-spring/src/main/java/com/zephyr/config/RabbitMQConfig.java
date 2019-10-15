package com.zephyr.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.zephyr.adapter.MessageDelegate;
import com.zephyr.converter.TextMessageConverter;

@Configuration
public class RabbitMQConfig {
	
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setVirtualHost(virtualHost);
		return connectionFactory;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}
	
	@Bean
	public TopicExchange exchange001() {
		return new TopicExchange("topic001", true, false);
	}

	@Bean
	public Queue queue001() {
		return new Queue("queue001", true);
	}

	@Bean
	public Binding binding001() {
		return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
	}
	
	@Bean
	public TopicExchange exchange002() {
		return new TopicExchange("topic002", true, false);
	}

	@Bean
	public Queue queue002() {
		return new Queue("queue002", true);
	}

	@Bean
	public Binding binding002() {
		return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
	}

	@Bean
	public TopicExchange exchange003() {
		return new TopicExchange("topic003", true, false);
	}

	@Bean
	public Queue queue003() {
		return new Queue("queue003", true);
	}

	@Bean
	public Binding binding003() {
		return BindingBuilder.bind(queue003()).to(exchange003()).with("mq.*");
	}
	
	@Bean
	public Queue imageQueue() {
		return new Queue("image_queue", true);
	}
	
	@Bean
	public Queue pdfQueue() {
		return new Queue("pdf_queue", true);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		return rabbitTemplate;
	}
	
	@Bean
	public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
		// 监听多个队列
		container.setQueues(queue001(), queue002(), queue003(), imageQueue(), pdfQueue());
		// 消费者的数量
		container.setConcurrentConsumers(1);
		// 消费者的最大数量
		container.setMaxConcurrentConsumers(5);
		// 是否重回队列
		container.setDefaultRequeueRejected(false);
		// 消息的签收模式：AUTO（自动签收）, MANUAL(手工签收)
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		// 设置是否将监听的channel暴露给已经注册的ChannelAwareMessageListener和 RabbitTemplate调用
		container.setExposeListenerChannel(true);
		// 消费者的标签生成策略
		container.setConsumerTagStrategy(new ConsumerTagStrategy() {
			
			@Override
			public String createConsumerTag(String queue) {
				return queue + "_" + UUID.randomUUID().toString();
			}
		});
		
		// 设置消息监听
//		container.setMessageListener(new ChannelAwareMessageListener() {
//			
//			@Override
//			public void onMessage(Message message, Channel channel) throws Exception {
//				String msg = new String(message.getBody());
//				System.err.println("监听到的消息：" + msg);
//			}
//		});
		
		/**
		 * 1、适配器方式一，
		 * 		默认方法的名字：handleMessage
		 * 		可以通过设置setDefaultListenerMethod的值，来更改默认的方法
		 * 		可以添加转换器，将字节数组转换成String
		 */
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//		adapter.setDefaultListenerMethod("consumeMessage");
//		adapter.setMessageConverter(new TextMessageConverter());
//		container.setMessageListener(adapter);
		
		/**
		 * 2、适配器方式二
		 */
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//		adapter.setMessageConverter(new TextMessageConverter());
//		Map<String, String> queueOrTagToMethodName = new HashMap<String, String>();
//		queueOrTagToMethodName.put("queue001", "method1");
//		queueOrTagToMethodName.put("queue002", "method2");
//		adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//		container.setMessageListener(adapter);
		
		/**
		 * 	支持JSON格式的转换器
		 */
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//		adapter.setDefaultListenerMethod("consumeMessage");
//		adapter.setMessageConverter(new Jackson2JsonMessageConverter());
//		container.setMessageListener(adapter);
		
		/***
		 *  DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
		 */
//		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//		adapter.setDefaultListenerMethod("consumeMessage");
//		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//		jackson2JsonMessageConverter.setJavaTypeMapper(new DefaultJackson2JavaTypeMapper());
//		adapter.setMessageConverter(jackson2JsonMessageConverter);
//		container.setMessageListener(adapter);
		
		/***
		 *  DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
		 */
		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
		adapter.setDefaultListenerMethod("consumeMessage");
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
		DefaultJackson2JavaTypeMapper jackson2JavaTypeMapper = new DefaultJackson2JavaTypeMapper();
		
		Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("order", com.zephyr.entity.Order.class);
		idClassMapping.put("packaged", com.zephyr.entity.Packaged.class);
		
		jackson2JavaTypeMapper.setIdClassMapping(idClassMapping);
		
		jackson2JsonMessageConverter.setJavaTypeMapper(jackson2JavaTypeMapper);
		adapter.setMessageConverter(jackson2JsonMessageConverter);
		container.setMessageListener(adapter);
		
		return container;
	}
}
