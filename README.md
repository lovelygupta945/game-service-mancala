Salman Azad Assignment Solution 
=======

A Java version of a board game!

## Getting Started

Instructions to up and run the application on your local machine for development and testing purposes. 

### Prerequisites

Things you need to get the project up and running on a local machine:

* Docker latest version
* docker-compose latest version

### Building and Running the application
#####  Approach 1
* Execute the below docker-compose command to build and run the application:
```
docker-compose up --build
```
#####  Approach 2
* Execute the below docker-compose command to start the database instance:
```
docker-compose up -d warehouse-db-postgres
```
##### and
* Execute the below mvn command to run the application:
```
./mvnw spring-boot:run
```

#### Swagger Endpoint

* Open 'http://localhost:8080/swagger-ui.html' in the web browser to open swagger to check REST apis.

#### REST services

* REST services can be accessed on: http://localhost:8080

#### Actuators 

* Can be accessed on: http://localhost:8080/actuator

Set-up
------------

The game has the following starting set up:

* Two players
* A board containing 2 sets of 6 pits for each player laid out in two lines of 6 facing each other, with one bigger "end pit" on the right of the player's 6 small pits
* 6 stones in each of the player's small pits.

The game has the following rules:
---------------------------------

* Players take turns to move stones from any of their small pits.
* In a move a player places the stones in every pit after the selected pit, anti-clockwise (towards right) round the board.
* In a move a player never places stones in their opponents big pit.
* If the move ends in the player's own big pit - they get another go.
* If the move ends in a player's own pit that was empty. They collect that last stone from that pit and all stones in the opponent's opposite pit.
* Play finishes when either player has no more moves to make (no more stones in any of small pits of either of the player).
* Winner of the game is the player who has the most stones in his big pit.

Technology/Framework used:
-------------------------
* Java 11, spring boot, Maven as build tool & Lombok library
* server : used embedded tomcat server through spring boot
* IDE : Intellij

Design:
------------
|Classes        | Purpose      | 
| ------------- |:-------------:| 
| **Player**    | Class to hold Game Player details | 
| **PitPlace**  | Enum containing representation of PitPlace (Upper, Lower)|  
| **GameStatus** | Enum representing Status of Game(WAIT_FOR_PLAYER, TIE, IN_PROGRESS, TIMEOUT, FIRST_PLAYER_WON, SECOND_PLAYER_WON)|
| **Game** | Class to represent Game (Id, Players, currentPlayer, Game Status) |
| **Board** | Class to represent Board (Pits in the board(small & Large, Game))|
| **GameState** | Class containing board Data, RoundStatus (WAIT_FOR_TURN, PLAY_TURN, FINISHED), Turn (PlayerId, PitPosition)|
| **Turn** | Class to represent playerId for the turn and pitPosition.|
| **RoundStatus** | Enum representing the status after one successful round of the game.|

API Design
-----------------

##### The api supports the following:
**Registration of Players**

| HTTP VERB| ENDPOINT |URL Params| DATA   | RESPONSE |
| ---------| -------- | -------- | ------ | -------- |
| PUT | player |{:id}| {"playerName":  String, "password":  String}| 200 OK|
| GET | player |none| none| {"id":  Long,	"playerName":  String, "password":  String}|

**Start or Join the Game**

| HTTP VERB| ENDPOINT |URL Params| DATA| RESPONSE |
| ------ | ------ | ------ | ------ | ------ |
| PUT | game | none | {"playerId":  Long}| 200 OK|
| GET | game |{:gameId}| none |  200 OK|  |
| GET | game/available | none |{"playerId":  Long}| 200 OK|
| GET | game/status |{:gameId}| none | 200 OK|
| POST | game/join |{:gameId} |{"playerId": Long}|  200 OK|

**Play the game**

| HTTP VERB| ENDPOINT |URL Params| DATA| RESPONSE |
| ------ | ------ | ------ | ------ | ------ |
| POST | play/move |none| {"playerID":  "1","pitPosition":  "3"}| 200 OK {"boardString":  String representing the board,"nextTurn":  Continue or wait for turn, "gameStatus": enum}|
| GET | move/checkTurn | none |{"playerID":  "1","pitPosition":  "3"}| 200 OK true or false|

**JSON Representing the Domain Objects:**

**Game:**
```json
{
    "id": 3,
    "secondPlayer": {
        "id": 2,
        "playerName": "testPlayer2",
        "password": "pass@2"
    },
    "firstPlayer": {
        "id": 1,
        "playerName": "testPlayer1",
        "password": "pass@1"
    },
    "gameStatus": "IN_PROGRESS",
    "currentPlayerId": 1,
    "created": "2020-06-08T10:34:46.806+00:00",
    "otherPlayerID": 2
}
```
**Player**
```json
 {
    "id": 1,
    "playerName": "testPlayer1",
    "password": "pass@1"
  }
```
**Board data after 10 moves**
```html
 Selected pitPosition: 0
  Large Stone Count: PLAYER A: 1
  Pit-0: 0...........6
 Pit-1: 7...........6
 Pit-2: 7...........6
 Pit-3: 7...........6
 Pit-4: 7...........6
 Pit-5: 7...........6
 Large Stone Count: PLAYER B: 0
  Selected pitPosition: 2
  Large Stone Count: PLAYER A: 2
  Pit-0: 0...........6
 Pit-1: 7...........6
 Pit-2: 0...........6
 Pit-3: 8...........7
 Pit-4: 8...........7
 Pit-5: 8...........7
 Large Stone Count: PLAYER B: 0
  Selected pitPosition: 3
  Large Stone Count: PLAYER A: 3
  Pit-0: 0...........6
 Pit-1: 7...........7
 Pit-2: 0...........7
 Pit-3: 0...........8
 Pit-4: 9...........8
 Pit-5: 9...........8
 Large Stone Count: PLAYER B: 0
  Selected pitPosition: 4
  Large Stone Count: PLAYER A: 3
  Pit-0: 1...........7
 Pit-1: 8...........0
 Pit-2: 1...........7
 Pit-3: 1...........8
 Pit-4: 10...........8
 Pit-5: 10...........9
 Large Stone Count: PLAYER B: 1
  Selected pitPosition: 5
  Large Stone Count: PLAYER A: 4
  Pit-0: 2...........8
 Pit-1: 9...........1
 Pit-2: 10...........0
 Pit-3: 1...........9
 Pit-4: 10...........9
 Pit-5: 0...........10
 Large Stone Count: PLAYER B: 1
  Selected pitPosition: 1
  Large Stone Count: PLAYER A: 4
  Pit-0: 3...........9
 Pit-1: 10...........2
 Pit-2: 11...........1
 Pit-3: 2...........10
 Pit-4: 10...........0
 Pit-5: 0...........10
 Large Stone Count: PLAYER B: 2
  Selected pitPosition: 0
  Large Stone Count: PLAYER A: 4
  Pit-0: 0...........9
 Pit-1: 11...........2
 Pit-2: 12...........1
 Pit-3: 3...........10
 Pit-4: 10...........0
 Pit-5: 0...........10
 Large Stone Count: PLAYER B: 2
  Selected pitPosition: 2
  Large Stone Count: PLAYER A: 4
  Pit-0: 1...........10
 Pit-1: 12...........3
 Pit-2: 13...........2
 Pit-3: 4...........0
 Pit-4: 11...........1
 Pit-5: 1...........11
 Large Stone Count: PLAYER B: 3
  Selected pitPosition: 3
  Large Stone Count: PLAYER A: 5
  Pit-0: 1...........10
 Pit-1: 12...........3
 Pit-2: 13...........2
 Pit-3: 0...........0
 Pit-4: 12...........1
 Pit-5: 0...........14
 Large Stone Count: PLAYER B: 3
  Selected pitPosition: 4
  Large Stone Count: PLAYER A: 5
  Pit-0: 2...........11
 Pit-1: 13...........0
 Pit-2: 14...........3
 Pit-3: 1...........1
 Pit-4: 13...........2
 Pit-5: 1...........15
 Large Stone Count: PLAYER B: 4
  Selected pitPosition: 5
  Large Stone Count: PLAYER A: 6
  Pit-0: 2...........11
 Pit-1: 13...........0
 Pit-2: 14...........3
 Pit-3: 1...........1
 Pit-4: 13...........2
 Pit-5: 0...........15
 Large Stone Count: PLAYER B: 4
  Selected pitPosition: 1
  Large Stone Count: PLAYER A: 7
  Pit-0: 3...........12
 Pit-1: 1...........1
 Pit-2: 15...........4
 Pit-3: 2...........2
 Pit-4: 14...........3
 Pit-5: 1...........16
 Large Stone Count: PLAYER B: 4

```