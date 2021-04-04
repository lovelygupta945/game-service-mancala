package com.bol.gameservice.exception;

public class PlayerAlreadyExistException extends RuntimeException {
    public PlayerAlreadyExistException(String msg) {
        super(msg);
    }
}
