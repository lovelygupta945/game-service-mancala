package com.bol.gameservice.service;

import com.bol.gameservice.domain.Board;
import com.bol.gameservice.domain.Game;
import com.bol.gameservice.domain.Player;
import com.bol.gameservice.dto.GameState;
import com.bol.gameservice.dto.GameStatus;
import com.bol.gameservice.repository.BoardRepository;
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
public class MoveServiceTest {

    @Mock
    private MoveService moveService;

    @Mock
    private BoardRepository boardRepository;

    private Game game;

    Player player1;

    Player player2;

    @BeforeEach
    public void before() {
        player1 = new Player(1L, "testPlayer1", "Pass@1");
        player2 = new Player(2L, "testPlayer2", "Pass@2");
        moveService = new MoveService(boardRepository);
        game = Game.builder()
                .firstPlayer(player1)
                .secondPlayer(player2)
                .gameStatus(GameStatus.IN_PROGRESS)
                .created(new Date())
                .currentPlayerId(player1.getId()).build();

    }

    @Test
    public void testPlayNextMove() {
        Mockito.when(boardRepository.findByGame(game))
               .thenReturn(Optional.of(new Board(0, 6, 6, game)));
        GameState gameState = moveService.playNextMove(game, 1L, 0);
        assertThat(gameState);
        assertThat(gameState.getGameStatus()).isEqualByComparingTo(GameStatus.IN_PROGRESS);
    }

    @Test
    public void testIsGameOver() {
        Mockito.when(boardRepository.findByGame(game))
                .thenReturn(Optional.of(new Board(0, 6, 6, game)));
        var gameStatus  = moveService.isGameOver(game);
        assertThat(gameStatus).isFalse();
    }

    @Test
    public void testGetWinner() {
        Mockito.when(boardRepository.findByGame(game))
               .thenReturn(Optional.of(new Board(0, 6, 6, game)));
        long winnerPlayerId  = moveService.getWinner(game);
        assertThat(winnerPlayerId).isEqualTo(player2.getId());
    }

    @Test
    public void isPlayerTurn() {
        assertThat(moveService.isPlayerTurn(game, player1.getId())).isTrue();
    }
}