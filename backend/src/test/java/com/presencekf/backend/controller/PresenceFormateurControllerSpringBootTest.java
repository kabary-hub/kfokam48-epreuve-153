package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PresenceFormateurControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    private Long sessionId;

    @BeforeEach
    void setUp() {
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setTitre("Cours test formateur");
        session.setCode("FORM01");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(1L);
        sessionRepository.save(session);
        sessionId = session.getId();
    }

    @Test
    void postPresencesFormateur_doitRetourner201AvecSourceFormateur() throws Exception {
        String body = """
                {
                    "sessionId": %d,
                    "etudiantId": 1
                }
                """.formatted(sessionId);

        mockMvc.perform(post("/api/presences/formateur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sessionId").value(sessionId))
                .andExpect(jsonPath("$.etudiantId").value(1))
                .andExpect(jsonPath("$.source").value("FORMATEUR"));
    }

    @Test
    void postPresencesFormateur_sessionInconnue_doitRetourner404() throws Exception {
        String body = """
                {
                    "sessionId": 999,
                    "etudiantId": 1
                }
                """;

        mockMvc.perform(post("/api/presences/formateur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SESSION_INCONNUE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postPresencesFormateur_sansSessionId_doitRetourner400() throws Exception {
        String body = """
                {
                    "etudiantId": 1
                }
                """;

        mockMvc.perform(post("/api/presences/formateur")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
