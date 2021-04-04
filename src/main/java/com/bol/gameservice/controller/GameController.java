package com.bol.gameservice.controller;


import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.GameStatus;
import com.bol.gameservice.exception.GameNotFoundException;
import com.bol.gameservice.exception.PlayerNotFoundException;
import com.bol.gameservice.service.GameService;
import com.bol.gameservice.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/game")
public class GameController {


    private final GameService gameService;

    private final PlayerService playerService;

    private final HttpSession httpSession;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, HttpSession httpSession){
        this.gameService = gameService;
        this.playerService = playerService;
        this.httpSession = httpSession;
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create new game")
    @ApiResponse(responseCode = "200", description = "Returns created game")
    public ResponseEntity<Game> createNewGame(@RequestBody Long playerId) {
        Game game = gameService
                .createNewGame(playerService.getPlayer(playerId).orElseThrow(
                        () -> new PlayerNotFoundException(playerId)));

        httpSession.setAttribute("gameId", game.getId());
        log.info(String.format("New game id: %s stored in session" , httpSession.getAttribute("gameId")));
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    }

    @GetMapping(value = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get game details")
    @ApiResponse(responseCode = "200", description = "Returns game")
    public ResponseEntity<Game> getGameDetails(@PathVariable Long gameId) {
        val game = gameService.getGame(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Get list of available games")
    @ApiResponse(responseCode = "200", description = "Returns list of available games")
    @GetMapping(value = "/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Game>> getGamesToJoin(@RequestBody Long playerId) {
        val game = gameService
                .getGamesToJoin(playerService.getPlayer(playerId)
                        .orElseThrow(() -> new PlayerNotFoundException(playerId)));
        return ResponseEntity.ok(game);
    }

    @PostMapping(value = "/join/{gameId}")
    @Operation(summary = "Join a game")
    @ApiResponse(responseCode = "200", description = "Returns joined game")
    public ResponseEntity<Game> joinGame(@RequestBody Long playerId, @PathVariable Long gameId) {
        val game = gameService.joinGame(playerService.getPlayer(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId))
                , gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        return ResponseEntity.ok(game);
    }

    @GetMapping(value = "/status/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get game status")
    @ApiResponse(responseCode = "200", description = "Returns game status")
    public ResponseEntity<GameStatus> getGameStatus(@PathVariable @NonNull Long gameId) {
         val game = gameService.getGameStatusByGameId(gameId);
        return ResponseEntity.ok(game.getGameStatus());
    }
}

