package org.jetbrains.assignment.model;

import java.util.List;

/**
 * A record representing a set of moves and corresponding positions.
 */
public record MoveSet(
    List<Move> move,
    List<Position> positions
) {
}
