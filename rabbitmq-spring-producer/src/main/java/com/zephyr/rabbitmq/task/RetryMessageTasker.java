package com.zephyr.rabbitmq.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zephyr.common.constant.Constants;
import com.zephyr.rabbitmq.dao.BrokerMessageLogMapper;
import com.zephyr.rabbitmq.domain.BrokerMessageLog;
import com.zephyr.rabbitmq.domain.Order;
import com.zephyr.rabbitmq.helper.RabbitSender;

@Component
public class RetryMessageTasker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RetryMessageTasker.class);

	@Autowired
	private BrokerMessageLogMapper brokerMessageLogMapper;
	
	@Autowired
	private RabbitSender rabbitSender;
	
	@Scheduled(initialDelay = 3000, fixedDelay = 10000)
	public void reSend(){
		LOGGER.info("---------------定时任务开始---------------");
		//pull status = 0 and timeout message 
		List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
		list.forEach(messageLog -> {
			if(messageLog.getTryCount() >= 3){
				//update fail message 
				brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), Constants.ORDER_SEND_FAILURE, new Date());
			} else {
				// resend 
				brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
				Order reSendOrder = JSON.parseObject(messageLog.getMessage(), Order.class);
				try {
					rabbitSender.sendOrder(reSendOrder);
				} catch (Exception e) {
					e.printStackTrace();
					LOGGER.error("-----------异常处理-----------");
				}
			}			
		});
		
	}
}
