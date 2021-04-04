package com.bol.gameservice.service;


import com.bol.gameservice.domain.Board;
import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.PitPlace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GameRulesTest {
    private Board board;
    private GameRules rules;

    @BeforeEach
    public void before() {
        Game game = mock(Game.class);
        board = new Board(0, 6, 6, game);
        rules = new GameRules();
    }

    @Test
    public void testPlayTurnValidPlay() {
        int pitPosition = 0;
        rules.playTurn(pitPosition, PitPlace.Lower, board);
        int[] pits = board.getPits();
        int indexOfLowerLargePit = board.getLowerLargePitIndex();
        int indexOfUpperLargePit = board.getUpperLargePitIndex();
        assertThat(1).isEqualTo(pits[indexOfLowerLargePit]);
        assertThat(0).isEqualTo(pits[0]);
        assertThat(7).isEqualTo(pits[1]);
        assertThat(0).isEqualTo(pits[indexOfUpperLargePit]);
    }

//    @Test( = PitPositionOutOfRangeException.class)
//    public void testPlayTurnInvalidPitPosition() {
//        int pitPosition = 6;
//        rules.playTurn(pitPosition, PitPlace.Lower, board);
//    }

    @Test
    public void tesPlayTurnCapturingOpponentStones() {
        int pitPosition = 5;
        int pitToCaptureTo = 5;
        int pitToCaptureFrom = 7;
        int[] pits = new int[14];
        pits[pitToCaptureTo] = 13;
        pits[pitToCaptureFrom] = 6;
        int stonesAfterCapture = pits[pitToCaptureFrom] + 2;
        board.setPits(pits);
        rules.playTurn(pitPosition, PitPlace.Lower, board);
        int[] pitsAfterPlay = board.getPits();

        assertThat(stonesAfterCapture).isEqualTo(pitsAfterPlay[pitToCaptureTo]);
        assertThat(0).isEqualTo(pitsAfterPlay[pitToCaptureFrom]);

    }

    @Test
    public void testGetWinnerTest() {
        int indexOfLowerLargePit = board.getLowerLargePitIndex();
        int indexOfUpperLargePit = board.getUpperLargePitIndex();

        int[] pits = new int[14];
        pits[indexOfLowerLargePit] = 13;
        pits[indexOfUpperLargePit] = 6;
        board.setPits(pits);
        PitPlace pitPlaceWinner = rules.getWinner(board);
        assertThat(PitPlace.Lower).isEqualTo(pitPlaceWinner);
    }
}
