package com.bol.gameservice.exception.handler;

import com.bol.gameservice.dto.ErrorDetails;
import com.bol.gameservice.exception.GameValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle exception
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Exception")
                        .details(exception.getMessage())
                        .build());
    }

    /**
     * Handle game validation exception
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = {GameValidationException.class})
    public ResponseEntity<ErrorDetails> handleGameValidationException(Exception exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Game Validation Exception")
                        .details(exception.getMessage())
                        .build());
    }

}

