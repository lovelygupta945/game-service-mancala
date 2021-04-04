package com.bol.gameservice.service;

import com.bol.gameservice.domain.Player;
import com.bol.gameservice.exception.PlayerAlreadyExistException;
import com.bol.gameservice.repository.PlayerRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Builder(toBuilder = true)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerService {

    private final PlayerRepository playerRepository;

    /**
     * Creates New Player
     * @param player player details
     * @return New played
     */
    public Player createNewPlayer(Player player) {
        val players = playerRepository.findAll();
        if(players.contains(player))
            throw new PlayerAlreadyExistException(String.format("Player Id %s already exists", player.getId()));
        val newPlayer = Player.builder()
                .playerName(player.getPlayerName())
                .password(player.getPassword())
                .build();
        playerRepository.save(newPlayer);
        return newPlayer;
    }

    /**
     *
     * @param playerId id of the player
     * @return Player
     */
    public Optional<Player> getPlayer(Long playerId) {
        return playerRepository.findById(playerId);
    }

    /**
     *
     * @return  List of  existing players
     */
    public List<Player> listPlayers() {
        return playerRepository.findAll();
    }
}

