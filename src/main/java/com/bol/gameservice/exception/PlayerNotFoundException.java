package com.bol.gameservice.exception;


public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long id) {
        super(String.format("Player Id %s does not exists", id));
    }
}

