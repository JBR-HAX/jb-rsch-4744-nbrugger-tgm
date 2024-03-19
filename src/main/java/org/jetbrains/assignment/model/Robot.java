package org.jetbrains.assignment.model;

public class Robot {
    private Position position;

    public Robot(Position position) {
        this.position = position;
    }

    public void move(Move move) {
        switch (move.dir()) {
            case EAST -> position = new Position(position.x() + 1, position.y());
            case WEST -> position = new Position(position.x() - 1, position.y());
            case NORTH -> position = new Position(position.x(), position.y() + 1);
            case SOUTH -> position = new Position(position.x(), position.y() - 1);
        }
    }

    public Position position() {
        return position;
    }
}
