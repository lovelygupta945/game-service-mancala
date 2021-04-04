package com.bol.gameservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({PlayerNotFoundException.class, GameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final String handleException(Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({IncorrectTurnPlayerException.class, PitPositionOutOfRangeException.class, PitHasNoStonesException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String handleTurnException(Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler({GameAlreadyStartedException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public final String handleConflictException(GameAlreadyStartedException ex) {
        return ex.getMessage();
    }

}