package com.sidy.sms_service.service;

import com.sidy.sms_service.dto.SmsRequest;
import com.sidy.sms_service.dto.SmsResponse;

public interface ISmsService {

    SmsResponse sendSms(SmsRequest request);

    String getProviderName();
}
