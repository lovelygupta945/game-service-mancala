package com.bol.gameservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.stream.IntStream;


@Data
@Entity
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "pits", nullable = false)
    private int[] pits;

    @Column(name = "lower_large_pit_index", nullable = false)
    private int lowerLargePitIndex;

    @Column(name = "upper_large_pit_index", nullable = false)
    private int upperLargePitIndex;

    public Board(int stonesInLarge, int pitSize, int stoneCntSmallPit, Game game) {

        this.pits = new int[2 * pitSize + 2];
        this.lowerLargePitIndex = pitSize;
        this.upperLargePitIndex = pitSize * 2 + 1;
        IntStream.range(0, upperLargePitIndex)
                .forEach(index -> this.pits[index] = stoneCntSmallPit);
        this.pits[lowerLargePitIndex] = stonesInLarge;
        this.pits[upperLargePitIndex] = stonesInLarge;
        this.game = game;
    }

    /***
     *
     * @return since the board is represented by one array this method returns the half of the array
     * which is size of pits assigned to a given player
     */
    public int getPitRange() {
        return (pits.length / 2 - 2);
    }

    /**
     *
     * @return prints the current state od board.
     * no of pits in board and count in each pit including the larger pits
     */
    @Override
    public String toString() {
        StringBuilder smallPit = new StringBuilder(" ");
        smallPit.append("Large Stone Count: PLAYER A: ")
                .append(pits[lowerLargePitIndex])
                .append("\n ");

        IntStream.range(0, lowerLargePitIndex).forEach(index -> {
            smallPit.append("Pit-")
                    .append(index).append(": ")
                    .append(pits[index])
                    .append("...........")
                    .append(+pits[upperLargePitIndex - index - 1])
                    .append("\n");
        });
        smallPit.append("Large Stone Count: PLAYER B: ")
                .append(pits[upperLargePitIndex])
                .append("\n ");
        return smallPit.toString();
    }
}
