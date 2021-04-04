package com.bol.gameservice;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.domain.Player;
import com.bol.gameservice.dto.GameStatus;
import com.bol.gameservice.repository.GameRepository;
import com.bol.gameservice.repository.PlayerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@Log4j2
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(PlayerRepository playerRepository, GameRepository gameRepository) {
        return args -> {
            Player player1 = Player.builder()
                    .id(1L)
                    .playerName("testPlayer1")
                    .password("pass@1")
                    .build();
            Player player2 = Player.builder()
                    .id(2L)
                    .playerName("testPlayer2")
                    .password("pass@2")
                    .build();
            Game game = Game.builder()
                    .firstPlayer(player1)
                    .secondPlayer(player2)
                    .gameStatus(GameStatus.IN_PROGRESS)
                    .created(new Date())
                    .currentPlayerId(player1.getId())
                    .build();
            log.info("Preloading of Player 1" + playerRepository.save(player1));
            log.info("Preloading of Player 2" + playerRepository.save(player2));
            log.info("Preloading of New Game: " + gameRepository.save(game));
        };
    }
}
