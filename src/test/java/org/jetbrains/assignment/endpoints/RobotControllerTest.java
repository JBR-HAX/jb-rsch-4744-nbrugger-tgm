package org.jetbrains.assignment.endpoints;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RobotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLocationsEndpoint() throws Exception {
        String requestBody = "[{\"direction\":\"EAST\",\"steps\":1},{\"direction\":\"NORTH\",\"steps\":3},{\"direction\":\"EAST\",\"steps\":3},{\"direction\":\"SOUTH\",\"steps\":5},{\"direction\":\"WEST\",\"steps\":2}]";

        mockMvc.perform(post("/locations")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].x", is(0)))
                .andExpect(jsonPath("$[0].y", is(0)))
                .andExpect(jsonPath("$[1].x", is(1)))
                .andExpect(jsonPath("$[1].y", is(0)))
                .andExpect(jsonPath("$[2].x", is(1)))
                .andExpect(jsonPath("$[2].y", is(3)))
                .andExpect(jsonPath("$[3].x", is(4)))
                .andExpect(jsonPath("$[3].y", is(3)))
                .andExpect(jsonPath("$[4].x", is(4)))
                .andExpect(jsonPath("$[4].y", is(-2)))
                .andExpect(jsonPath("$[5].x", is(2)))
                .andExpect(jsonPath("$[5].y", is(-2)));
    }

    @Test
    public void testMovesEndpoint() throws Exception {
        String requestBody = "[{\"x\": 0, \"y\": 0}, {\"x\": 1, \"y\": 0}, {\"x\": 1, \"y\": 3}, {\"x\": 0, \"y\": 3}, {\"x\": 0, \"y\": 0}]";

        mockMvc.perform(post("/moves")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].direction", is("EAST")))
                .andExpect(jsonPath("$[0].steps", is(1)))
                .andExpect(jsonPath("$[1].direction", is("NORTH")))
                .andExpect(jsonPath("$[1].steps", is(3)))
                .andExpect(jsonPath("$[2].direction", is("WEST")))
                .andExpect(jsonPath("$[2].steps", is(1)))
                .andExpect(jsonPath("$[3].direction", is("SOUTH")))
                .andExpect(jsonPath("$[3].steps", is(3)));
    }
}