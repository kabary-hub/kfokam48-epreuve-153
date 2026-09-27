package com.presencekf.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postSessions_doitRetourner201AvecChampsContractuels() throws Exception {
        String body = """
                {
                    "titre": "Cours de test intégration",
                    "promotionId": 1
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.ouvertureAt").exists())
                .andExpect(jsonPath("$.expirationAt").exists());
    }
}
