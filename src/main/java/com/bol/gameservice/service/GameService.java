 package com.bol.gameservice.service;


 import com.bol.gameservice.domain.Game;
 import com.bol.gameservice.domain.Player;
 import com.bol.gameservice.dto.GameStatus;
 import com.bol.gameservice.exception.GameAlreadyStartedException;
 import com.bol.gameservice.exception.GameNotFoundException;
 import com.bol.gameservice.exception.PlayerAlreadyAddedToGameException;
 import com.bol.gameservice.repository.GameRepository;
 import lombok.RequiredArgsConstructor;
 import lombok.extern.log4j.Log4j2;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import javax.inject.Inject;
 import java.util.Date;
 import java.util.List;
 import java.util.Optional;
 import java.util.stream.Collectors;


 @Service
 @Transactional
 @Log4j2
 @RequiredArgsConstructor(onConstructor_ = @Inject)
 public class GameService {

     private final GameRepository gameRepository;
     /**
      * Creates a new game
      * @param player Player for the game
      * @return New Game Created.
      */
     public Game createNewGame(Player player) {
         return gameRepository.findByFirstPlayer(player).orElseGet(() -> {
             Game game = Game.builder()
                     .firstPlayer(player)
                     .gameStatus(GameStatus.WAIT_FOR_PLAYER)
                     .created(new Date())
                     .build();
             gameRepository.save(game);
             return game;
         });
     }

     /**
      * Update the game.
      * @param game Existing game
      * @param gameStatus Status of the game that needs to be updated.
      * @return Updated game.
      */
     public Optional<Game> updateGame(Game game, GameStatus gameStatus) {
         return getGame(game.getId()).map(foundGame -> {
             foundGame.setGameStatus(gameStatus);
             gameRepository.save(foundGame);
             return foundGame;
         });
     }

     /**
      * Get list of available games to join.
      * @param player Player requesting for available games.
      * @return Returns list of available games to join
      */
     public List<Game> getGamesToJoin(Player player) {
         return gameRepository.findByGameStatus(GameStatus.WAIT_FOR_PLAYER)
                 .stream().filter(game -> game.getFirstPlayer() != player)
                 .collect(Collectors.toList());

     }

     /**
      *
      * @param player Player to join game.
      * @param gameId Game to join
      * @return Joined game.
      */
     public Optional<Game> joinGame(Player player, Long gameId) {
         return getGame(gameId).map(foundGame -> {
             if(foundGame.getGameStatus() == GameStatus.IN_PROGRESS) {
                throw new GameAlreadyStartedException(gameId);
             }
             if(foundGame.getOtherPlayerID() == player.getId()){
                 throw new PlayerAlreadyAddedToGameException(player.getId(), gameId);
             }
             foundGame.setSecondPlayer(player);
             gameRepository.save(foundGame);
             updateGame(foundGame, GameStatus.IN_PROGRESS);
             return foundGame;
         });
     }

     /**
      *
       * @param gameId Game id to find game
      * @return Get Game Status of the game.
      */
     public Game getGameStatusByGameId(Long gameId) {
         return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
     }

     /**
      *
      * @param player Player in progress game.
      * @return List of game.
      */
     public List<Game> getPlayerGames(Player player) {
         return gameRepository.findByGameStatus(
                 GameStatus.IN_PROGRESS).stream().filter(game -> game.getFirstPlayer() == player).collect(Collectors.toList());
     }

     /**
      *
      * @param id Game id.
      * @return Game.
      */
     public Optional<Game> getGame(Long id) {
         return gameRepository.findById(id);
     }
 }
