package com.bol.gameservice.controller;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.GameState;
import com.bol.gameservice.dto.Turn;
import com.bol.gameservice.exception.GameNotFoundException;
import com.bol.gameservice.service.GameService;
import com.bol.gameservice.service.MoveService;
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
    public ResponseEntity<GameState> playNextMove(@RequestBody Turn currentTurn) {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        GameState gameState = moveService.playNextMove(game, currentTurn.getPlayerID(), currentTurn.getPitPosition());
        httpSession.setAttribute("gameStatus", gameState);
        gameService.updateGame(game, gameState.getGameStatus());
        return new ResponseEntity<>(gameState, HttpStatus.OK);
    }

    @GetMapping("/move/checkTurn")
    public ResponseEntity<Boolean> checkTurn(@RequestBody Turn currentTurn) {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Game game = gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(0L));
        return new ResponseEntity<>(moveService.isPlayerTurn(game, currentTurn.getPlayerID()), HttpStatus.OK);
    }
}
