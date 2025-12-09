package com.sidy.sms_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class SmsRequest {

    @JsonProperty("to")
    @NotBlank(message = "Le numéro de destination est obligatoire")
    private String to;

    @JsonProperty("message")
    @NotBlank(message = "Le message est obligatoire")
    private String message;

}