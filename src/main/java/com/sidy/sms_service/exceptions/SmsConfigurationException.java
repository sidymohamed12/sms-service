package com.sidy.sms_service.exceptions;

public class SmsConfigurationException extends SmsServiceException {

    public SmsConfigurationException(String message, String provider) {
        super(message, provider, "CONFIG_ERROR");
    }

    public SmsConfigurationException(String message, String provider, Throwable cause) {
        super(message, provider, "CONFIG_ERROR", cause);
    }
}