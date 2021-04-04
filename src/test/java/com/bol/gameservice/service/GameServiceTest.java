package com.bol.gameservice.service;

import com.bol.gameservice.domain.Game;
import com.bol.gameservice.domain.Player;
import com.bol.gameservice.dto.GameStatus;
import com.bol.gameservice.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    private Player player1;

    private Player player2;

    @BeforeEach
    public void before() {
        gameService = new GameService(gameRepository);
        player1 = new Player(1L, "testPlayer1", "Test@123");
        player2 = new Player(2L, "testPlayer2", "Test@123");
    }

    @Test
    public void testGetGame() {
        long gameId = 3L;
        Mockito.when(gameRepository.findById(gameId)).thenReturn(Optional.of(getGame()));
        Optional<Game> game = gameService.getGame(gameId);
        assertThat(game);
    }

    private Game getGame() {
        return Game.builder()
                .firstPlayer(player1)
                .secondPlayer(player2)
                .gameStatus(GameStatus.IN_PROGRESS)
                .created(new Date())
                .currentPlayerId(player1.getId()).build();
    }

    @Test
    public void testCreateNewGame() {
        Mockito.when(gameRepository.save(getGame())).thenReturn(getGame());
        Game newGame = gameService.createNewGame(player1);
        assertThat(newGame);
    }

    @Test
    public void testUpdateGame() {
        Mockito.when(gameRepository.save(getGame())).thenReturn(getGame());
        Optional<Game> updateGame = gameService.updateGame(getGame(), GameStatus.FIRST_PLAYER_WON);
        updateGame.ifPresent(game -> assertThat(game.getGameStatus().equals(GameStatus.FIRST_PLAYER_WON)));
    }

    @Test
    public void testJoinGame() {
        Mockito.when(gameRepository.save(getGame())).thenReturn(getGame());
        Optional<Game> updateGame = gameService.joinGame(new Player(3L), getGame().getId());
        updateGame.ifPresent(game -> assertThat(game.getSecondPlayer().getId() == 3L));
    }
}
