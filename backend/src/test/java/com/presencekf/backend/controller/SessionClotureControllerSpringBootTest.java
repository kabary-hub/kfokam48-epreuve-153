package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionClotureControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    private Long sessionId;

    @BeforeEach
    void setUp() {
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setTitre("Cours test clôture");
        session.setCode("CLOT01");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(1L);
        sessionRepository.save(session);
        sessionId = session.getId();
    }

    @Test
    void postCloture_doitRetourner200AvecClotureAt() throws Exception {
        mockMvc.perform(post("/api/sessions/" + sessionId + "/cloture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionId))
                .andExpect(jsonPath("$.clotureAt").exists());
    }

    @Test
    void postCloture_sessionInconnue_doitRetourner404() throws Exception {
        mockMvc.perform(post("/api/sessions/99999/cloture"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SESSION_INCONNUE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postCloture_dejaCloturee_doitRetourner409() throws Exception {
        mockMvc.perform(post("/api/sessions/" + sessionId + "/cloture"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/sessions/" + sessionId + "/cloture"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SESSION_DEJA_CLOTUREE"))
                .andExpect(jsonPath("$.message").exists());
    }
}
