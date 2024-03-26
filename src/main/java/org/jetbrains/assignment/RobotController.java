package org.jetbrains.assignment;

import org.jetbrains.assignment.dtos.Location;
import org.jetbrains.assignment.dtos.Move;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RobotController {

    private final RobotMoveRepository repository;

    public RobotController(RobotMoveRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/moves")
    public List<Move> move(@RequestBody List<Location> locations) {
        if (locations.isEmpty()) {
            return List.of();
        }
        var lastPos = locations.get(0);
        var moves = new ArrayList<Move>();
        for (var location : locations.subList(1, locations.size())) {
            var deltaX = location.x() - lastPos.x();
            var deltaY = location.y() - lastPos.y();
            if (deltaX != 0) {
                final var xMovement = new Move(deltaX > 0 ? Move.Direction.EAST : Move.Direction.WEST, Math.abs(deltaX));
                moves.add(xMovement);
                //storing inside a loop is of course dangerous, but this is just an example and simple
                //this should be collected and at the end stored in one transaction/bulk inser
                repository.storeMoveTo(xMovement, location);
            }
            if (deltaY != 0) {
                final var yMove = new Move(deltaY > 0 ? Move.Direction.NORTH : Move.Direction.SOUTH, Math.abs(deltaY));
                moves.add(yMove);
                repository.storeMoveTo(yMove, location);
            }
            lastPos = location;
        }
        return moves;
    }

    @PostMapping("/locations")
    public List<Location> locations(@RequestBody List<Move> moves) {
        var location = new Location(0,0);
        var locations = new ArrayList<Location>();
        locations.add(location);
        for (var move : moves) {
            //storing inside a loop is of course dangerous, but this is just an example and simple
            //this should be collected and at the end stored in one transaction/bulk insert
            repository.storeMoveFrom(location, move);
            switch (move.direction()) {
                case EAST -> location = new Location(location.x() + move.steps(), location.y());
                case WEST -> location = new Location(location.x() - move.steps(), location.y());
                case NORTH -> location = new Location(location.x(), location.y() + move.steps());
                case SOUTH -> location = new Location(location.x(), location.y() - move.steps());
            }
            locations.add(location);
        }
        return locations;
    }
}
