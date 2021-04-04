package com.bol.gameservice.repository;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.domain.Player;
import com.bol.gameservice.dto.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByGameStatus(GameStatus gameStatus);

    Optional<Game> findById(Long gameId);

    Optional<Game> findByFirstPlayer(Player player);
}
