package com.bol.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the Turn of the Players (player Id and the position in the pit to pick stones)
 */

@Data
@AllArgsConstructor
public class Turn {
    long playerID;
    int pitPosition;
}
