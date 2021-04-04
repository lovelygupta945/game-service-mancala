package com.bol.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the current state of the game (board, player turn, game status {@Link GameStatus})
 */
@Data
@AllArgsConstructor
public class GameState {
    String boardData;
    RoundStatus nextTurn;
    GameStatus gameStatus;

    @Override
    public String toString() {
        return nextTurn + "\n";
    }
}
