package com.sidy.sms_service.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsProperties {

    private String provider;
    private List<String> failoverProviders;

    private TwilioConfig twilio = new TwilioConfig();
    private InfobipConfig infobip = new InfobipConfig();

    @Data
    public static class TwilioConfig {
        private String accountSid;
        private String authToken;
        private String fromNumber;
    }

    @Data
    public static class InfobipConfig {
        private String apiKey;
        private String baseUrl;
        private String fromNumber;
    }
}