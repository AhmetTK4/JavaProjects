package com.example.gameoflife;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GridControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void firstCallReturnsInitialBoardAndNextCallAdvancesIt() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(get("/nextGeneration").param("rows", "4").param("cols", "6").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].length()").value(6));
        Grid initial = (Grid) session.getAttribute(GridController.SESSION_KEY);

        mvc.perform(get("/nextGeneration").param("rows", "4").param("cols", "6").session(session))
                .andExpect(status().isOk());
        Grid second = (Grid) session.getAttribute(GridController.SESSION_KEY);
        assertEquals(initial.next().toString(), second.toString());
    }

    @Test
    void sessionsDoNotShareBoards() throws Exception {
        MockHttpSession first = new MockHttpSession();
        MockHttpSession second = new MockHttpSession();
        mvc.perform(get("/nextGeneration").param("rows", "5").param("cols", "5").session(first));
        mvc.perform(get("/nextGeneration").param("rows", "8").param("cols", "8").session(second));
        assertEquals(5, ((Grid) first.getAttribute(GridController.SESSION_KEY)).rows());
        assertEquals(8, ((Grid) second.getAttribute(GridController.SESSION_KEY)).rows());
    }

    @Test
    void rejectsBoardsOutsideAllowedSize() throws Exception {
        mvc.perform(get("/nextGeneration").param("rows", "100000").param("cols", "100000"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/nextGeneration").param("rows", "0").param("cols", "5"))
                .andExpect(status().isBadRequest());
    }
}
