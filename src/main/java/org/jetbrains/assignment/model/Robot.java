package org.jetbrains.assignment.model;

public class Robot {
    private Position position;

    public Robot(Position position) {
        this.position = position;
    }

    public void move(Move move) {
        switch (move.direction()) {
            case EAST -> position = new Position(position.x() + move.steps(), position.y());
            case WEST -> position = new Position(position.x() - move.steps(), position.y());
            case NORTH -> position = new Position(position.x(), position.y() + move.steps());
            case SOUTH -> position = new Position(position.x(), position.y() - move.steps());
        }
    }

    public Position position() {
        return position;
    }
}
