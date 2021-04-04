package com.bol.gameservice.exception;


public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super(String.format("Game Id %s does not exists", id));
    }
}

