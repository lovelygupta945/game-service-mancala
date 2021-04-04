package com.bol.gameservice.exception;

import com.bol.gameservice.dto.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle not found exception
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = {PlayerNotFoundException.class, GameNotFoundException.class})
    public ResponseEntity<ErrorDetails> handleNotFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Not found Exception")
                        .details(exception.getMessage())
                        .build());
    }

    /**
     * Handle turn exception
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = {IncorrectTurnPlayerException.class, PitPositionOutOfRangeException.class, PitHasNoStonesException.class})
    public ResponseEntity<ErrorDetails> handleTurnException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Bad request")
                        .details(exception.getMessage())
                        .build());
    }

    /**
     * Handle conflict exception
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(value = {GameAlreadyStartedException.class})
    public ResponseEntity<ErrorDetails> handleConflictException(Exception exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .message("Game Already started")
                        .details(exception.getMessage())
                        .build());
    }
}