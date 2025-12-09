package com.sidy.sms_service.exceptions;

public class SmsProviderException extends SmsException {

    private final String provider;

    public SmsProviderException(String provider, String message) {
        super(String.format("Erreur avec le provider %s: %s", provider, message));
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}