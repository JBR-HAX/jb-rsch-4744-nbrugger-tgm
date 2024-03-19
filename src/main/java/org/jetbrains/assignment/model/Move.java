package org.jetbrains.assignment.model;

public record Move(
        Direction direction,
        int steps
) {
    public enum Direction {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }
}
