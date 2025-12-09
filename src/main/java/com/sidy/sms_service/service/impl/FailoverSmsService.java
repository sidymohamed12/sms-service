package com.sidy.sms_service.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sidy.sms_service.config.SmsProperties;
import com.sidy.sms_service.dto.SmsRequest;
import com.sidy.sms_service.dto.SmsResponse;
import com.sidy.sms_service.exceptions.SmsException;
import com.sidy.sms_service.exceptions.SmsProviderException;
import com.sidy.sms_service.service.ISmsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FailoverSmsService implements ISmsService {

    private final Map<String, ISmsService> providers = new HashMap<>();
    private final SmsProperties smsProperties;

    public FailoverSmsService(
            @Qualifier("twilioSmsService") ISmsService twilioService,
            @Qualifier("infobipSmsService") ISmsService infobipService,
            SmsProperties smsProperties) {

        providers.put("twilio", twilioService);
        providers.put("infobip", infobipService);
        this.smsProperties = smsProperties;
    }

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        List<String> tryProviders = new ArrayList<>();
        tryProviders.add(smsProperties.getProvider());
        if (smsProperties.getFailoverProviders() != null) {
            tryProviders.addAll(smsProperties.getFailoverProviders());
        }

        for (String providerName : tryProviders) {
            ISmsService service = providers.get(providerName.toLowerCase());
            if (service == null)
                continue;

            try {
                log.info("Attempting to send SMS via {}", providerName);
                SmsResponse response = service.sendSms(request);
                log.info("SMS sent successfully via {}", providerName);
                return response;
            } catch (SmsProviderException e) {
                log.error("Provider {} failed: {}", providerName, e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error with provider {}: {}", providerName, e.getMessage());
            }
        }

        throw new SmsException("Tous les providers SMS ont échoué");
    }

    @Override
    public String getProviderName() {
        return smsProperties.getProvider();
    }

}
