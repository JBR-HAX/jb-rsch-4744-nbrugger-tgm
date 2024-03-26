package org.jetbrains.assignment;

import org.jetbrains.assignment.dtos.Location;
import org.jetbrains.assignment.dtos.Move;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@MockitoSettings
class RobotControllerTest {

    @Mock
    RobotMoveRepository repository;
    @InjectMocks
    RobotController controller;


    @Nested
    class move {
        @Test
        void noLocations() {
            var movesFromNoLocations = controller.move(List.of());
            assertThat(movesFromNoLocations).isEmpty();
        }

        /**
         * When there is only one location, there are no moves to make.
         */
        @Test
        void singleLocation() {
            var movesFromSingleLocation = controller.move(List.of(new Location(0, 0)));
            assertThat(movesFromSingleLocation).isEmpty();
        }

        @Test
        void multipleLocations() {
            var locations = List.of(
                    new Location(0, 0),
                    new Location(1, 0),
                    new Location(1, 1),
                    new Location(0, 1),
                    new Location(0, 0)
            );
            var movesFromMultipleLocations = controller.move(locations);
            assertThat(movesFromMultipleLocations).containsExactly(
                    new Move(Move.Direction.EAST, 1),
                    new Move(Move.Direction.NORTH, 1),
                    new Move(Move.Direction.WEST, 1),
                    new Move(Move.Direction.SOUTH, 1)
            );
            verify(repository, times(movesFromMultipleLocations.size())).storeMoveTo(any(), any());
        }
    }

    @Nested
    class locations {
        @Test
        void noMoves() {
            var locationsFromNoMoves = controller.locations(List.of());
            assertThat(locationsFromNoMoves).containsExactly(new Location(0, 0));
        }

        @Test
        void singleMove() {
            var locationsFromSingleMove = controller.locations(List.of(new Move(Move.Direction.EAST, 1)));
            assertThat(locationsFromSingleMove).containsExactly(
                    new Location(0, 0),
                    new Location(1, 0)
            );
        }

        @Test
        void multipleMoves() {
            var moves = List.of(
                    new Move(Move.Direction.EAST, 1),
                    new Move(Move.Direction.NORTH, 1),
                    new Move(Move.Direction.WEST, 1),
                    new Move(Move.Direction.SOUTH, 1)
            );
            var locationsFromMultipleMoves = controller.locations(moves);
            assertThat(locationsFromMultipleMoves).containsExactly(
                    new Location(0, 0),
                    new Location(1, 0),
                    new Location(1, 1),
                    new Location(0, 1),
                    new Location(0, 0)
            );
            verify(repository, times(moves.size())).storeMoveFrom(any(), any());
        }
    }

    @Nested
    @SpringBootTest
    @AutoConfigureMockMvc
    class IntegrationTest {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void testLocationsEndpoint() throws Exception {
            String requestBody = "[{\"direction\":\"EAST\",\"steps\":1},{\"direction\":\"NORTH\",\"steps\":3}," +
                    "{\"direction\":\"EAST\",\"steps\":3},{\"direction\":\"SOUTH\",\"steps\":5},{\"direction\":\"WEST\",\"steps\":2}]";

            mockMvc.perform(MockMvcRequestBuilders.post("/locations")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("[{\"x\":0,\"y\":0},{\"x\":1,\"y\":0},{\"x\":1,\"y\":3}," +
                            "{\"x\":4,\"y\":3},{\"x\":4,\"y\":-2},{\"x\":2,\"y\":-2}]"));
        }

        @Test
        void testMovesEndpoint() throws Exception {
            String requestBody = "[{\"x\": 0, \"y\": 0}, {\"x\": 1, \"y\": 0}, {\"x\": 1, \"y\": 3}, {\"x\": 0, \"y\": 3}, {\"x\": 0, \"y\": 0}]";

            mockMvc.perform(MockMvcRequestBuilders.post("/moves")
                            .contentType("application/json")
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("[{\"direction\":\"EAST\",\"steps\":1}," +
                            "{\"direction\":\"NORTH\",\"steps\":3},{\"direction\":\"WEST\",\"steps\":1},{\"direction\":\"SOUTH\",\"steps\":3}]"));
        }
    }
}