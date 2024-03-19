package org.jetbrains.assignment.endpoints;

import org.jetbrains.assignment.dao.RoboMoveDebugDao;
import org.jetbrains.assignment.model.Move;
import org.jetbrains.assignment.model.Position;
import org.jetbrains.assignment.model.Robot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RobotApi {
    private final RoboMoveDebugDao dao;

    public RobotApi(RoboMoveDebugDao dao) {
        this.dao = dao;
    }

    @PostMapping("/locations")
    public List<Position> getLocations(@RequestBody List<Move> moves) {
        var robot = new Robot(new Position(0, 0));
        var steps = new ArrayList<Position>(moves.size());
        steps.add(robot.position());
        for (Move move : moves) {
            robot.move(move);
            steps.add(robot.position());
            //this should never be done in a real application! insert in loop bad
            dao.persistMove(move, robot.position().x(), robot.position().y());
        }

        return steps;
    }

    @PostMapping("/moves")
    public List<Move> move(@RequestBody List<Position> locations) {
        //From the docs it is not clear if the robot should start at position 0,0 or at the first position in the list.
        //I assume that it is the first element in the list, unless the list is empty then it is 0,0
        //If a "0" step move is valid is questionable
        if (locations.isEmpty()) return List.of(new Move(Move.Direction.NORTH, 0));

        //if there is only one element in the list the start position is assumed to be 0,0 too
        //again the assignment doesn't cover edge cases like this
        final var robotStartsAtNullPoint = locations.size() == 1;
        var robot = new Robot(robotStartsAtNullPoint ? new Position(0, 0) : locations.getFirst());
        var moves = new ArrayList<Move>(locations.size());
        for (int i = robotStartsAtNullPoint ? 0 : 1; i < locations.size(); i++) {
            var to = locations.get(i);
            while (!robot.position().equals(to)) {
                final var from = robot.position();
                var direction = direction(from, to);
                var steps = switch (direction) {
                    case EAST -> to.x() - from.x();
                    case WEST -> from.x() - to.x();
                    case NORTH -> to.y() - from.y();
                    case SOUTH -> from.y() - to.y();
                };
                var move = new Move(direction, steps);
                moves.add(move);
                robot.move(move);
                dao.persistMove(move, robot.position().x(), robot.position().y());
            }
        }
        return moves;
    }

    public Move.Direction direction(Position from, Position to) {
        if (from.x() < to.x()) return Move.Direction.EAST;
        if (from.x() > to.x()) return Move.Direction.WEST;
        if (from.y() < to.y()) return Move.Direction.NORTH;
        if (from.y() > to.y()) return Move.Direction.SOUTH;
        else throw new IllegalArgumentException("from and to are the same");
    }
}
