package com.bol.gameservice.service;

import com.bol.gameservice.domain.Board;
import com.bol.gameservice.dto.PitPlace;
import com.bol.gameservice.dto.RoundStatus;
import com.bol.gameservice.exception.PitHasNoStonesException;
import com.bol.gameservice.exception.PitPositionOutOfRangeException;
import lombok.extern.log4j.Log4j2;

/***
 *  This class contains the main logic of game.
 */
@Log4j2
public class GameRules {
    /***
     *
     * @param pitPosition the pit position to start play from.
     * @param pitPlace pit place represent the pit lower or upper depending on player id.
     *                 lower part of pit array is assigned to player with lower id.
     * @param board  the class to represent the board. stores the state of game
     * @return RoundStatus which determines if the same player should continue or not.
     */
    protected RoundStatus playTurn(int pitPosition, PitPlace pitPlace, Board board) {
        int[] pits = board.getPits();
        int stonesInPit = pits[pitPosition];

        checkTurnPriorConditions(stonesInPit, pitPosition, board);

        int lowerLargePitIndex = board.getLowerLargePitIndex();
        int upperLargePitIndex = board.getUpperLargePitIndex();
        if (pitPlace == PitPlace.Upper)
            pitPosition = pitPosition + lowerLargePitIndex + 1;

        pits[pitPosition] = 0;
        int index = sowStones(pitPosition, pitPlace, lowerLargePitIndex, upperLargePitIndex, stonesInPit, pits);

        // capturing stones
        int [] updatedPits = pits[index] == 1 ? capturingStone(index - 1, pits, upperLargePitIndex, lowerLargePitIndex) : pits;
        board.setPits(updatedPits);

        // check turn
        if (pitPlace == PitPlace.Upper && (index - 1) == upperLargePitIndex)
            return RoundStatus.PLAY_TURN;

        if (pitPlace == PitPlace.Lower && (index - 1) == lowerLargePitIndex)
            return RoundStatus.PLAY_TURN;

        return RoundStatus.WAIT_FOR_TURN;
    }

    /**
     * This method checked the conditions that needs to fulfilled before playing the turn.
     * @param stonesInPit No. of stones in the selected Pits.
     * @param pitPosition Selected position in the pits.
     * @param board Board representing the game including pits.
     */
    private void checkTurnPriorConditions(int stonesInPit, int pitPosition, Board board) {

        if (pitPosition < 0 || pitPosition > (board.getPitRange())) {
            throw new PitPositionOutOfRangeException(String.format("Pit Position: %s is out of range. Use range [ 0-5 ] ", pitPosition));
        }
        if(stonesInPit == 0) {
            throw new PitHasNoStonesException(String.format("Pit %s has no stones. Please pick stones from another pit.", pitPosition));
        }
    }

    /**
     * Sow stones in the pits as per the rule not in opponent's large pit.
     *
     * @param pitPosition Position in the Pit to pick stones.
     * @param pitPlace Place in the pit UPPER or LOWER.
     * @param lowerLargePitIndex Index of Upper Large Pit (Player 2's large pit).
     * @param upperLargePitIndex Index of Lower Large Pit (Player 1's Large pit)
     * @param stonesInPit No. of stones in the selected pit.
     * @param pits array to represent pits in the board.
     * @return New index of the after sowing all the stones.
     */
    private int sowStones(int pitPosition, PitPlace pitPlace, int lowerLargePitIndex, int upperLargePitIndex, int stonesInPit, int [] pits) {
        int index = pitPosition + 1;
        while (stonesInPit > 0) {
            if (index > upperLargePitIndex) {
                index = 0;
            }
            if (!((index == upperLargePitIndex && pitPlace == PitPlace.Lower) || (index == lowerLargePitIndex && pitPlace == PitPlace.Upper))) {
                pits[index]++;
                stonesInPit--;
            }
            index++;
        }
        return index;
    }

    /**
     * This method checks if the game is over.
     * @param board Board representing the game.
     * @return Returns true or false.
     */
    protected boolean isGameOver(Board board) {
        int lowerLargePitIndex = board.getLowerLargePitIndex();
        int[] pits = board.getPits();

        for (int index = 0; index < lowerLargePitIndex; index++) {
            if (pits[index] != 0) return false;
            if (pits[lowerLargePitIndex + index + 1] != 0) return false;
        }
        return true;
    }

    /**
     * This method returns the winner of the game.
     * @param board Board representing the game.
     * @return Returns PitPlace which determines the winner Upper -> Player 2, Lower -> Player 1
     */
    protected PitPlace getWinner(Board board) {
        int[] pits = board.getPits();
        return pits[board.getLowerLargePitIndex()] > pits[board.getUpperLargePitIndex()] ? PitPlace.Lower : PitPlace.Upper;
    }

    /**
     *
     * @param index current index of pit array.
     * @param pits pits representing the board
     * @param upperLargePitIndex Index Of Upper Large Pit
     * @param lowerLargePitIndex Index of Lower Large Pit
     * @return new pit array after collecting the stones as per game rule.
     */
    private int[] capturingStone(int index, int[] pits, int upperLargePitIndex, int lowerLargePitIndex) {
            int oppositeIndex = upperLargePitIndex - index - 1;
            if (!(index == lowerLargePitIndex || index == upperLargePitIndex)) {
                log.info("capturing stones..............");
                pits[index] = pits[index] + pits[oppositeIndex];
                pits[oppositeIndex] = 0;
            }
        return pits;
    }
}
