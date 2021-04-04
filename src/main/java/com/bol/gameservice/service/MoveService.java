package com.bol.gameservice.service;

import com.bol.gameservice.domain.Board;
import com.bol.gameservice.domain.Game;
import com.bol.gameservice.dto.GameState;
import com.bol.gameservice.dto.GameStatus;
import com.bol.gameservice.dto.PitPlace;
import com.bol.gameservice.dto.RoundStatus;
import com.bol.gameservice.exception.GameNotFoundException;
import com.bol.gameservice.exception.IncorrectTurnPlayerException;
import com.bol.gameservice.exception.PitPositionOutOfRangeException;
import com.bol.gameservice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MoveService {

    private final BoardRepository boardRepository;
    private GameRules gameRules = new GameRules();

    /**
     * Checks if the given playerId has current Turn.
     * @param game game
     * @param playerId id of the player
     * @return true or false
     */
    public boolean isPlayerTurn(Game game, Long playerId) {
        return game.getCurrentPlayerId() == playerId;
    }

    /**
     *
     * @param game Game that is being played
     * @param playerId id of the player
     * @param pitPosition selected index in the pit array
     * @return GameState
     */
    public GameState playNextMove(Game game, Long playerId, int pitPosition) {
        RoundStatus roundStatus;

        // As per the task the board will contain 6 pits and 6 stones in each pit.
        Board board = boardRepository.findByGame(game).orElseGet(() -> new Board(0, 6, 6, game));
        if (game.getCurrentPlayerId() != playerId) {
            String errorMsg = String.format("Not your turn. WAIT for other player id: %s to play.  ", playerId);
            log.error(errorMsg);
            throw new IncorrectTurnPlayerException(errorMsg);
        }

        if (pitPosition < 0 || pitPosition > (board.getPitRange())) {
            String errorMsg = String.format("Specified Pit Position: %s out of range. Use range [ 0-5 ] " , pitPosition);
            log.error(errorMsg);
            throw new PitPositionOutOfRangeException(errorMsg);
        }

        roundStatus = game.getCurrentPlayerId() > game.getOtherPlayerID()
                        ? gameRules.playTurn(pitPosition, PitPlace.Upper, board)
                        : gameRules.playTurn(pitPosition, PitPlace.Lower, board);

        boardRepository.save(board);
        return calculateGameState(board, game, roundStatus);
    }

    /**
     *
     * @param board Board representing the pits.
     * @param game Game that is being played
     * @param roundStatus Current RoundStatus after any round.
     * @return GameState
     */
    private GameState calculateGameState(Board board, Game game, RoundStatus roundStatus) {
        if (!gameRules.isGameOver(board)) {
            if (roundStatus == RoundStatus.PLAY_TURN) {
                return new GameState(board.toString(), RoundStatus.PLAY_TURN, GameStatus.IN_PROGRESS);
            } else {
                game.setCurrentPlayerId(game.getOtherPlayerID());
                return new GameState(board.toString(), RoundStatus.WAIT_FOR_TURN, GameStatus.IN_PROGRESS);
            }
        } else {
            // update game status
            PitPlace pitPlace = gameRules.getWinner(board);
            return pitPlace == PitPlace.Upper ? new GameState(board.toString(), RoundStatus.FINISHED, GameStatus.SECOND_PLAYER_WON) :
                    new GameState(board.toString(), RoundStatus.FINISHED, GameStatus.FIRST_PLAYER_WON);
        }
    }

    /**
     *
     * @param game Game
     * @return true or false
     */
    public boolean isGameOver(Game game) {
        Board board = boardRepository.findByGame(game).orElseThrow(() -> new GameNotFoundException(game.getId()));
        return gameRules.isGameOver(board);
    }

    /**
     * Returns the playerId of the winner in the game
     * @param game Game that is being played
     * @return Returns the playerId of the winner in the game.
     */
    public Long getWinner(Game game) {
        Board board = boardRepository.findByGame(game).orElseThrow(() -> new GameNotFoundException(game.getId()));

        return gameRules.getWinner(board) == PitPlace.Upper
                ? game.getCurrentPlayerId() > game.getOtherPlayerID() ? game.getCurrentPlayerId() : game.getOtherPlayerID()
                : game.getCurrentPlayerId() < game.getOtherPlayerID() ? game.getCurrentPlayerId() : game.getOtherPlayerID();
    }
}
