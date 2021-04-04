package com.bol.gameservice.dto;

/**
 * Represents the status of the game.
 */
public enum GameStatus {
    WAIT_FOR_PLAYER,
    IN_PROGRESS,
    FIRST_PLAYER_WON,
    SECOND_PLAYER_WON,
    TIE,
    TIMEOUT
}



