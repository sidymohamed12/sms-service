package com.sidy.sms_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.sidy.sms_service.config.SmsProperties;

@SpringBootApplication
@EnableConfigurationProperties(SmsProperties.class)
public class SmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsServiceApplication.class, args);
	}

}
