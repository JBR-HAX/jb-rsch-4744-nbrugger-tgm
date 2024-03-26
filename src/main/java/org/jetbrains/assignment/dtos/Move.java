package org.jetbrains.assignment.dtos;

public record Move(
        Direction direction,
        int steps
) {
    public enum Direction {
        SOUTH,
        NORTH,
        WEST,
        EAST
    }
}
