package com.bol.gameservice.service;

import com.bol.gameservice.domain.Player;
import com.bol.gameservice.exception.PlayerAlreadyExistException;
import com.bol.gameservice.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PlayerServiceTest {

    @Mock
    PlayerRepository playerRepository;

    Player player1;

    Player player2;

    PlayerService playerService;

    @BeforeEach
    public void before() {
        playerService = new PlayerService(playerRepository);
        player1 = new Player(1L, "testPlayer1", "Test@123");
        player2 = new Player(2L, "testPlayer2", "Test@123");
    }

    @Test
    public void testListPlayers() {
        Mockito.when(playerRepository.findAll()).thenReturn(List.of(player1, player2));
        var playerList = playerService.listPlayers();
        assertThat(playerList).isNotNull();
        assertThat(playerList.get(0)).isEqualTo(player1);
    }

    @Test
    public void testCreateNewPlayer() {
        Mockito.when(playerRepository.findAll()).thenReturn(List.of(player1, player2));
        var newPlayer = playerService.createNewPlayer(new Player(5L, "testNewPlayer", "pass@3"));
        assertThat(newPlayer).isNotNull();
    }

    @Test
    public void testCreateNewPlayerThrowsException() {
        Mockito.when(playerRepository.findAll()).thenReturn(List.of(player1, player2));
        try{
            assertThat(playerService.createNewPlayer(player1));
        } catch (PlayerAlreadyExistException ex){
            assertThatExceptionOfType(PlayerAlreadyExistException.class);
        }

    }

    @Test
    public void testGetPlayer() {
        Mockito.when(playerRepository.findById(player1.getId())).thenReturn(Optional.ofNullable(player1));
        assertThat(playerService.getPlayer(player1.getId())).isNotNull();
    }
}