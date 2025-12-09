package com.sidy.sms_service.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sidy.sms_service.dto.SmsRequest;
import com.sidy.sms_service.dto.SmsResponse;
import com.sidy.sms_service.service.ISmsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "SMS API", description = "Envoi de SMS via plusieurs providers")
@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
@Slf4j
public class SmsController {

    private final ISmsService smsService;

    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(
            @Valid @RequestBody SmsRequest request) {

        log.info("DTO: {}", request);

        SmsResponse response = smsService.sendSms(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/provider")
    public ResponseEntity<Map<String, String>> getProvider() {
        return ResponseEntity.ok(
                Map.of("provider", smsService.getProviderName()));
    }
}
