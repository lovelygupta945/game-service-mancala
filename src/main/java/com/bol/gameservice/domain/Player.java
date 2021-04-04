package com.bol.gameservice.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "player_name", unique = true, nullable = false)
    @NonNull
    private String playerName;
    @NonNull
    @Column(name = "password", nullable = false)
    private String password;

    public Player(long id) {

    }
}
