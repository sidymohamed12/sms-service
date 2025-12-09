package com.sidy.sms_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.sidy.sms_service.service.ISmsService;
import com.sidy.sms_service.service.impl.FailoverSmsService;

@Configuration
public class SmsConfiguration {

    @Bean
    @Primary
    public ISmsService smsService(FailoverSmsService failoverSmsService) {
        return failoverSmsService;
    }

}