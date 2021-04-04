package com.bol.gameservice.domain;


import com.bol.gameservice.dto.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "second_player_id", nullable = true)
    private Player secondPlayer;

    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;

    @Enumerated(EnumType.STRING)
    @Column
    private GameStatus gameStatus;

    @Column(name = "current_player_id", nullable = false)
    private long currentPlayerId;

    @Column(name = "created", nullable = false)
    private Date created;

    public long getOtherPlayerID() {
        return firstPlayer.getId() == currentPlayerId ? secondPlayer.getId() : firstPlayer.getId();
    }
}
