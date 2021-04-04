package com.bol.gameservice.controller;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.*;
import com.bol.gameservice.exception.GameNotFoundException;
import com.bol.gameservice.exception.PlayerNotFoundException;
import com.bol.gameservice.service.GameService;
import com.bol.gameservice.service.MoveService;
import com.bol.gameservice.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Log4j2
public class DemoPlayGameController {

    private final GameService gameService;
    private final PlayerService playerService;
    private final MoveService moveService;

    @Autowired
    public DemoPlayGameController(GameService gameService, PlayerService playerService, MoveService moveService){
        this.gameService = gameService;
        this.playerService = playerService;
        this.moveService = moveService;
    }


    /**
     *
     * @param request playGame request
     * @return Board representation
     */

    @PutMapping(value = "/demoPlayGame", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Play demo game")
    @Operation(summary = "Play Game")
    public ResponseEntity<String> playGame(@RequestBody PlayGameRequest request) {
        long player1 = request.getPlayer1();
        long player2 = request.getPlayer2();
        long gameId = request.getGameId();

        Game game = checkGamePriorConditionAndGetGame(player1, player2, gameId);
        Turn turn = request.getTurn() != null ? request.getTurn() : new Turn(player1, 0);
        game.setCurrentPlayerId(turn.getPlayerID());

        GameState gameState = new GameState("Game Started", RoundStatus.PLAY_TURN, GameStatus.IN_PROGRESS);
        int[] pitPositionInput = {0, 2, 3, 4, 5, 1};
        AtomicInteger steps = new AtomicInteger(1);
        StringBuilder result = new StringBuilder();

        while (gameState.getGameStatus() == GameStatus.IN_PROGRESS) {
            Arrays.stream(pitPositionInput).limit(10).forEach(pitPosition -> {
                val newGameState = moveService.playNextMove(game, game.getCurrentPlayerId(), pitPosition);
                steps.getAndIncrement();
                result.append("Selected pitPosition:").append(pitPosition).append("\n");
                result.append(newGameState.getBoardData());
                log.info(result);
            });
            if(steps.get() >= 10) {
                break;
            }
        }

        return ResponseEntity.ok(result.toString());
    }

    private Game checkGamePriorConditionAndGetGame(long player1, long player2, long gameId) {
        playerService.getPlayer(player1).orElseThrow(
                () -> new PlayerNotFoundException(player1));
        playerService.getPlayer(player1).orElseThrow(
                () -> new PlayerNotFoundException(player2));
        return gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
    }
}
