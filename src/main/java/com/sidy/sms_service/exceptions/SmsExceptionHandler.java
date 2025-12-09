package com.sidy.sms_service.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sidy.sms_service.dto.SmsResponse;

import kong.unirest.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class SmsExceptionHandler {

        @ExceptionHandler(SmsProviderException.class)
        public ResponseEntity<SmsResponse> handleSmsProviderException(
                        SmsProviderException ex) {
                log.error("SMS Provider Error: {}", ex.getMessage());

                SmsResponse response = SmsResponse.builder()
                                .success(false)
                                .provider(ex.getProvider())
                                .errorMessage(ex.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(response);
        }

        @ExceptionHandler(SmsException.class)
        public ResponseEntity<SmsResponse> handleSmsException(SmsException ex) {
                log.error("SMS Error: {}", ex.getMessage());

                SmsResponse response = SmsResponse.builder()
                                .success(false)
                                .errorMessage(ex.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach(error -> {
                        String fieldName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        errors.put(fieldName, errorMessage);
                });

                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("errors", errors);
                response.put("timestamp", LocalDateTime.now());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(response);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<SmsResponse> handleGenericException(Exception ex) {
                log.error("Unexpected error", ex);

                SmsResponse response = SmsResponse.builder()
                                .success(false)
                                .errorMessage("Une erreur inattendue s'est produite")
                                .timestamp(LocalDateTime.now())
                                .build();

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(response);
        }
}
