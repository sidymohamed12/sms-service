package com.sidy.sms_service.service.impl;

import org.springframework.stereotype.Service;

import com.sidy.sms_service.config.SmsProperties;
import com.sidy.sms_service.dto.SmsRequest;
import com.sidy.sms_service.dto.SmsResponse;
import com.sidy.sms_service.exceptions.SmsProviderException;
import com.sidy.sms_service.service.ISmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service("twilioSmsService")
@Slf4j
public class TwilioSmsService implements ISmsService {

    private final SmsProperties smsProperties;

    public TwilioSmsService(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @PostConstruct
    public void init() {
        Twilio.init(
                smsProperties.getTwilio().getAccountSid(),
                smsProperties.getTwilio().getAuthToken());
        log.info("Twilio SMS Service initialized");
    }

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        try {
            log.info("Sending SMS via Twilio to: {}", request.getTo());

            Message message = Message.creator(
                    new PhoneNumber(request.getTo()),
                    new PhoneNumber(smsProperties.getTwilio().getFromNumber()),
                    request.getMessage()).create();

            log.info("SMS sent successfully via Twilio. SID: {}", message.getSid());

            return SmsResponse.builder()
                    .success(true)
                    .messageId(message.getSid())
                    .provider("twilio")
                    .status(message.getStatus().toString())
                    .build();

        } catch (Exception e) {
            log.error("Error sending SMS via Twilio", e);
            throw new SmsProviderException("Twilio", e.getMessage());
        }
    }

    @Override
    public String getProviderName() {
        return "twilio";
    }
}