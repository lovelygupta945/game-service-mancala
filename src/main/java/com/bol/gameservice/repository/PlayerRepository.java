package com.bol.gameservice.repository;

import com.bol.gameservice.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findOneByPlayerName(String playerName);
}
