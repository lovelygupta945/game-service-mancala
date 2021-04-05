package com.bol.gameservice.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * Class to accept request for DemoPlayerController.
 */

@Data
public class PlayGameRequest {
    @NonNull Long player1;
    @NonNull Long player2;
    @NonNull Long gameId;
    Turn turn;
}
