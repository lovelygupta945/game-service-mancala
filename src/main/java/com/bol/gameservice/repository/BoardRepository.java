package com.bol.gameservice.repository;

import com.bol.gameservice.domain.Board;
import com.bol.gameservice.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByGame(Game game);
}
