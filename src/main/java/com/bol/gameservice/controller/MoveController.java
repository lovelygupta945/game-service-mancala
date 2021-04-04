package com.bol.gameservice.controller;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.GameState;
import com.bol.gameservice.dto.Turn;
import com.bol.gameservice.exception.GameNotFoundException;
import com.bol.gameservice.service.GameService;
import com.bol.gameservice.service.MoveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MoveController {

    private final HttpSession httpSession;

    private final MoveService moveService;

    private final GameService gameService;

    @Autowired
    public MoveController(HttpSession httpSession, MoveService moveService, GameService gameService){
        this.httpSession = httpSession;
        this.moveService = moveService;
        this.gameService = gameService;
    }

    @PostMapping("/play/move")
    @ApiResponse(responseCode = "200", description = "Returns game state")
    @Operation(summary = "Play next move")
    public ResponseEntity<GameState> playNextMove(@RequestBody Turn currentTurn) {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        GameState gameState = moveService.playNextMove(game, currentTurn.getPlayerID(), currentTurn.getPitPosition());
        httpSession.setAttribute("gameStatus", gameState);
        gameService.updateGame(game, gameState.getGameStatus());
        return ResponseEntity.ok(gameState);
    }

    @GetMapping("/move/checkTurn")
    @ApiResponse(responseCode = "200", description = "Return player's turn is valid")
    @Operation(summary = "Check turn")
    public ResponseEntity<Boolean> checkTurn(@RequestBody Turn currentTurn) {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(0L));
        return ResponseEntity.ok(moveService.isPlayerTurn(game, currentTurn.getPlayerID()));
    }
}
