package com.sidy.sms_service.service.impl;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponseDetails;
import com.infobip.model.SmsTextualMessage;
import com.sidy.sms_service.config.SmsProperties;
import com.sidy.sms_service.dto.SmsRequest;
import com.sidy.sms_service.dto.SmsResponse;
import com.sidy.sms_service.exceptions.SmsProviderException;
import com.sidy.sms_service.service.ISmsService;
import com.twilio.exception.ApiException;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("infobipSmsService")
public class InfobipSmsService implements ISmsService {

    private final SmsProperties smsProperties;
    private SmsApi smsApi;

    public InfobipSmsService(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing Infobip SMS Service...");
            log.debug("Base URL: {}", smsProperties.getInfobip().getBaseUrl());

            // Vérifier que l'URL contient le protocole
            String baseUrl = smsProperties.getInfobip().getBaseUrl();
            if (!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
                baseUrl = "https://" + baseUrl;
                log.warn("Protocol missing in base URL, adding https://");
            }

            // Créer l'objet ApiKey
            ApiKey apiKey = ApiKey.from(smsProperties.getInfobip().getApiKey());

            // Construire le client avec ApiKey et BaseUrl
            ApiClient apiClient = ApiClient.forApiKey(apiKey)
                    .withBaseUrl(BaseUrl.from(baseUrl))
                    .build();

            this.smsApi = new SmsApi(apiClient);
            log.info("Infobip SMS Service initialized successfully");

        } catch (Exception e) {
            log.error("Failed to initialize Infobip SMS Service", e);
            throw new IllegalStateException(
                    "Impossible d'initialiser le service Infobip: " + e.getMessage(), e);
        }
    }

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        try {
            log.info("Sending SMS via Infobip to: {}", request.getTo());

            SmsTextualMessage message = new SmsTextualMessage()
                    .from(smsProperties.getInfobip().getFromNumber())
                    .destinations(Collections.singletonList(
                            new SmsDestination().to(request.getTo())))
                    .text(request.getMessage());

            SmsAdvancedTextualRequest smsRequest = new SmsAdvancedTextualRequest()
                    .messages(Collections.singletonList(message));

            // Appel synchrone avec execute() - retourne SmsResponse d'Infobip
            com.infobip.model.SmsResponse infobipResponse = smsApi
                    .sendSmsMessage(smsRequest)
                    .execute();

            SmsResponseDetails messageResult = infobipResponse.getMessages().get(0);

            log.info("SMS sent successfully via Infobip. MessageId: {}",
                    messageResult.getMessageId());

            return SmsResponse.builder()
                    .success(true)
                    .messageId(messageResult.getMessageId())
                    .provider("infobip")
                    .status(messageResult.getStatus().getName())
                    .build();

        } catch (ApiException e) {
            log.error("Error sending SMS via Infobip: {}", e.getMessage(), e);
            throw new SmsProviderException("Infobip", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error sending SMS via Infobip", e);
            throw new SmsProviderException("Infobip",
                    "Erreur inattendue: " + e.getMessage());
        }
    }

    @Override
    public String getProviderName() {
        return "infobip";
    }
}
