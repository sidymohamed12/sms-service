package com.sidy.sms_service.exceptions;

public class SmsServiceException extends RuntimeException {

    private final String provider;
    private final String errorCode;

    public SmsServiceException(String message, String provider, String errorCode) {
        super(message);
        this.provider = provider;
        this.errorCode = errorCode;
    }

    public SmsServiceException(String message, String provider, String errorCode, Throwable cause) {
        super(message, cause);
        this.provider = provider;
        this.errorCode = errorCode;
    }

    public String getProvider() {
        return provider;
    }

    public String getErrorCode() {
        return errorCode;
    }
}