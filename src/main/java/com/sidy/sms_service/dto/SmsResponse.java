package com.sidy.sms_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponse {

    private boolean success;
    private String messageId;
    private String provider;
    private String status;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String errorMessage;
}