package com.bol.gameservice.controller;

import com.bol.gameservice.domain.Player;
import com.bol.gameservice.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }

    @PutMapping(value = "/{id}")
    @ApiResponse(responseCode = "200", description = "Returns player account")
    @Operation(summary = "Create player account")
    public ResponseEntity<Player> createAccount(@RequestBody Player player, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playerService.getPlayer(id).map(foundPlayer -> {
            foundPlayer.setPlayerName(player.getPlayerName());
            foundPlayer.setPassword(player.getPassword());
            return foundPlayer;
        }).orElseGet(() -> playerService.createNewPlayer(player)));
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Return list of players")
    @Operation(summary = "Get players")
    public ResponseEntity<List<Player>> getPlayers() {
        return ResponseEntity.ok(playerService.listPlayers());
    }
}
