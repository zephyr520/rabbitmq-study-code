package com.zephyr.rabbitmq.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zephyr.common.constant.Constants;
import com.zephyr.common.utils.DateUtils;
import com.zephyr.rabbitmq.dao.BrokerMessageLogMapper;
import com.zephyr.rabbitmq.dao.OrderMapper;
import com.zephyr.rabbitmq.domain.BrokerMessageLog;
import com.zephyr.rabbitmq.domain.Order;
import com.zephyr.rabbitmq.helper.RabbitSender;

@Service
public class OrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	@Autowired
	private RabbitSender rabbitSender;
	
	public void createOrder(Order order) throws Exception {
		// order current time 
		Date orderTime = new Date();
		// order insert
		orderMapper.insertSelective(order);
		// log insert
		BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
		brokerMessageLog.setMessageId(order.getMessageId());
		//save order message as json
		brokerMessageLog.setMessage(JSON.toJSONString(order));
		brokerMessageLog.setStatus("0");
		brokerMessageLog.setNextRetry(DateUtils.addMinute(orderTime, Constants.ORDER_TIMEOUT));
		brokerMessageLog.setCreateTime(new Date());
		brokerMessageLog.setUpdateTime(new Date());
		brokerMessageLogMapper.insert(brokerMessageLog);
		// order message sender
		rabbitSender.sendOrder(order);
	}
}
