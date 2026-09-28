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
class PresenceControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    private String code;

    @BeforeEach
    void setUp() {
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setTitre("Cours test présence");
        session.setCode("CODE01");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(1L);
        sessionRepository.save(session);
        code = session.getCode();
    }

    @Test
    void postPresences_doitRetourner201AvecSourceEtudiant() throws Exception {
        String body = """
                {
                    "code": "%s",
                    "etudiantId": 1
                }
                """.formatted(code);

        mockMvc.perform(post("/api/presences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sessionId").exists())
                .andExpect(jsonPath("$.etudiantId").value(1))
                .andExpect(jsonPath("$.source").value("ETUDIANT"));
    }

    @Test
    void postPresences_codeInconnu_doitRetourner400() throws Exception {
        String body = """
                {
                    "code": "ZZZZZZ",
                    "etudiantId": 1
                }
                """;

        mockMvc.perform(post("/api/presences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("CODE_INCONNU"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postPresences_cinqCodesInconnus_doitBloquerAvec429() throws Exception {
        String body = """
                {
                    "code": "ZZZZZZ",
                    "etudiantId": 9001
                }
                """;

        for (int attempt = 0; attempt < 5; attempt++) {
            mockMvc.perform(post("/api/presences")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("CODE_INCONNU"));
        }

        mockMvc.perform(post("/api/presences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.code").value("TROP_TENTATIVES"))
                .andExpect(jsonPath("$.message").exists());
    }
}
