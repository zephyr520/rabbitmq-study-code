package com.zephyr;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zephyr.rabbitmq.domain.Order;
import com.zephyr.rabbitmq.service.OrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringProducerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private OrderService orderService;

	@Test
	public void testCreateOrder() throws Exception {
		Order order = new Order();
		order.setId("2018080400000005");
		order.setName("测试创建订单");
		order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
		orderService.createOrder(order);
	}
}
