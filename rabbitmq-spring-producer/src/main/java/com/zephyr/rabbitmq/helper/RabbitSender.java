package com.zephyr.rabbitmq.helper;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zephyr.common.constant.Constants;
import com.zephyr.rabbitmq.dao.BrokerMessageLogMapper;
import com.zephyr.rabbitmq.domain.Order;

@Component
public class RabbitSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitSender.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	// 回调函数：confirm确认
	final ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
		
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			LOGGER.info("correlationData: {}", correlationData.getId());
			String messageId = correlationData.getId();
			if (ack) {
				// 如果confirm返回成功 则进行更新
				brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS, new Date());
			} else {
				//失败则进行具体的后续操作:重试 或者补偿等手段
				LOGGER.info("异常处理...");
			}
		}
	};
	
	public void sendOrder(Order order) {
		rabbitTemplate.setConfirmCallback(confirmCallback);
		//消息唯一ID
		CorrelationData correlationData = new CorrelationData(order.getMessageId());
		rabbitTemplate.convertAndSend("order-exchange11", "order.ABC", order, correlationData);
	}
}
