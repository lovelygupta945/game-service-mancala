package com.bol.gameservice.dto;

/**
 * Represents the status of the round. After a round completed, if same player gets another chance or its other player's chance.
 */
public enum RoundStatus {
    SUCCESS,
    FAILURE,
    WAIT_FOR_TURN,
    PLAY_TURN,
    FINISHED
}
