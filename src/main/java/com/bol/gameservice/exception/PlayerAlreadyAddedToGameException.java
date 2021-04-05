package com.bol.gameservice.exception;


public class PlayerAlreadyAddedToGameException extends RuntimeException {
    public PlayerAlreadyAddedToGameException(Long playerId, Long gameId) {
        super(String.format("Player id %s already added to game id %s", playerId, gameId));
    }
}

