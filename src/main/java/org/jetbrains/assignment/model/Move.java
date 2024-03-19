package org.jetbrains.assignment.model;

public record Move(
        Direction dir,
        int steps
) {
    public enum Direction {
        EAST,
        WEST,
        NORTH,
        SOUTH
    }
}
