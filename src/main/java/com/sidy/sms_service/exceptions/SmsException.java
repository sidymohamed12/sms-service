package com.sidy.sms_service.exceptions;

public class SmsException extends RuntimeException {

    public SmsException(String message) {
        super(message);
    }

    public SmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
