package org.jetbrains.assignment;

import org.jetbrains.assignment.dtos.Location;
import org.jetbrains.assignment.dtos.Move;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class RobotMoveRepository {
    private final DSLContext db;

    public RobotMoveRepository(DSLContext db) {
        this.db = db;
    }

    public void storeMoveFrom(Location from, Move move) {
        db.insertInto(table("move_event"))
                .columns(field("event_type"), field("x_cord"), field("y_cord"), field("direction"), field("steps"))
                .values("move_from_location", from.x(), from.y(), move.direction().name(), move.steps())
                .execute();
    }

    public void storeMoveTo(Move move, Location to) {
        db.insertInto(table("move_event"))
                .columns(field("event_type"), field("x_cord"), field("y_cord"), field("direction"), field("steps"))
                .values("move_to_location", to.x(), to.y(), move.direction().name(), move.steps())
                .execute();
    }
}
