package com.bol.gameservice.exception;


public class GameAlreadyStartedException extends RuntimeException {
    public GameAlreadyStartedException(Long id) {
        super(String.format("Game id %s already started", id));
    }
}

