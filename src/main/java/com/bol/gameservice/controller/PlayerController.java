package com.bol.gameservice.controller;

import com.bol.gameservice.domain.Player;
import com.bol.gameservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    PlayerService playerService;

    @PutMapping(value = "/{id}")
    public ResponseEntity<Player> createAccount(@RequestBody Player player, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.getPlayer(id).map(foundPlayer -> {
            foundPlayer.setPlayerName(player.getPlayerName());
            foundPlayer.setPassword(player.getPassword());
            return foundPlayer;
        }).orElseGet(() -> playerService.createNewPlayer(player)));
    }

    @GetMapping
    public ResponseEntity<List<Player>> getPlayers() {
        return new ResponseEntity<>(playerService.listPlayers(), HttpStatus.OK);
    }
}
