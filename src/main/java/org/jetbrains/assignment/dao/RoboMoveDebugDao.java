package org.jetbrains.assignment.dao;

import org.jetbrains.assignment.model.Move;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

@Service
public class RoboMoveDebugDao {

    private final DSLContext db;

    public RoboMoveDebugDao(DSLContext db) {
        this.db = db;
    }

    public void persistMove(Move move, int x, int y) {
        db.insertInto(DSL.table("move"))
                .columns(DSL.field("direction"), DSL.field("steps"), DSL.field("x"), DSL.field("y"))
                .values(move.direction().name(), move.steps(), x, y)
                .execute();
    }
}
