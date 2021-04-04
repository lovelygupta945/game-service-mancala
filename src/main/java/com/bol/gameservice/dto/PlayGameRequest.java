package com.bol.gameservice.dto;

import lombok.Data;

/**
 * Class to accept request for DemoPlayerController.
 */

@Data
public class PlayGameRequest {
    Long player1;
    Long player2;
    Long gameId;
    Turn turn;
}
