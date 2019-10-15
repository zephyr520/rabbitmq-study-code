package com.zephyr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zephyr.rabbitmq.dao")
public class RabbitmqSpringProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqSpringProducerApplication.class, args);
	}

}
