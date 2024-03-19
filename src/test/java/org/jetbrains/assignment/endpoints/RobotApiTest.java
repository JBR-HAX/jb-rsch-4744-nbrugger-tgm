package org.jetbrains.assignment.endpoints;

import org.jetbrains.assignment.dao.RoboMoveDebugDao;
import org.jetbrains.assignment.model.Move;
import org.jetbrains.assignment.model.Position;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jetbrains.assignment.model.Move.Direction.EAST;
import static org.jetbrains.assignment.model.Move.Direction.NORTH;

@SuppressWarnings("ALL")
@MockitoSettings
class RobotApiTest {

    @Mock
    RoboMoveDebugDao db;

    @InjectMocks
    private RobotApi api;

    @Test
    void direction() {
        assertThat(api.direction(new Position(0, 0), new Position(1, 0))).isEqualTo(EAST);
        assertThat(api.direction(new Position(0, 0), new Position(0, 1))).isEqualTo(Move.Direction.NORTH);
        assertThat(api.direction(new Position(0, 0), new Position(-1, 0))).isEqualTo(Move.Direction.WEST);
        assertThat(api.direction(new Position(0, 0), new Position(0, -1))).isEqualTo(Move.Direction.SOUTH);
    }


    @Nested
    class GetLocations {
        @Test
        void returnsStartPositionOnEmptyMoves() {
            assertThat(api.getLocations(List.of())).first().isEqualTo(new Position(0, 0));
        }

        @Test
        void returnsEndPositionOnSingleMove() {
            assertThat(api.getLocations(List.of(new Move(EAST, 1)))).last().isEqualTo(new Position(1, 0));
        }

        @Test
        void returnsEndPositionOnMultipleMoves() {
            assertThat(api.getLocations(List.of(new Move(EAST, 1), new Move(EAST, 1)))).last().isEqualTo(new Position(2, 0));
        }

        @Test
        void returnsStartPositionOnMultipleMoves() {
            assertThat(api.getLocations(List.of(new Move(EAST, 1), new Move(EAST, 1)))).first().isEqualTo(new Position(0, 0));
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 50})
        void containsNPlusOnePositionOnNMoves(int i) {
            var steps = IntStream.generate(() -> 1).limit(i).mapToObj(j -> new Move(EAST, 1)).toList();
            assertThat(api.getLocations(steps)).hasSize(i + 1);
        }
    }

    @Nested
    class MoveApi {
        @Test
        void returnsNoOpMoveOnEmptyLocations() {
            assertThat(api.move(List.of())).first().extracting(it -> it.steps()).isEqualTo(0);
        }

        @Test
        void returnsSingleMoveOnSingleLocation() {
            assertThat(api.move(List.of(new Position(1, 0)))).containsExactly(new Move(EAST, 1));
        }

        @Test
        void returnsMultipleMovesOnMultipleLocations() {
            assertThat(api.move(List.of(new Position(1, 0), new Position(2, 0)))).containsExactly(new Move(EAST, 1));
        }

        @Test
        void splitsCornerMovesIntoTwo() {
            assertThat(api.move(List.of(new Position(0, 0), new Position(2, 2)))).containsExactlyInAnyOrder(new Move(NORTH, 2), new Move(EAST, 2));
        }
    }

}