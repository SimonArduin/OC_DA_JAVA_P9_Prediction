package com.medilabo.prediction.integration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PredictionIntegrationTests {

    @Autowired
    MockMvc mockMvc;
    @Test
    public void contextLoads() {}

    @Nested
    public class predictionTests {
        @Test
        public void predictionTestIfEarlyOnset() throws Exception {
            MvcResult result = mockMvc.perform(get("/get")
                            .param("id", String.valueOf(4)))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            assertEquals("Early onset", result.getResponse().getContentAsString());
        }

        @Test
        public void predictionTestIfDanger() throws Exception {
            MvcResult result = mockMvc.perform(get("/get")
                            .param("id", String.valueOf(3)))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            assertEquals("Danger", result.getResponse().getContentAsString());
        }

        @Test
        public void predictionTestIfBorderline() throws Exception {
            MvcResult result = mockMvc.perform(get("/get")
                            .param("id", String.valueOf(2)))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            assertEquals("Borderline", result.getResponse().getContentAsString());
        }
        @Test
        public void predictionTestIfNone() throws Exception {
            MvcResult result = mockMvc.perform(get("/get")
                            .param("id", String.valueOf(1)))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();

            assertEquals("None", result.getResponse().getContentAsString());
        }
    }
}
