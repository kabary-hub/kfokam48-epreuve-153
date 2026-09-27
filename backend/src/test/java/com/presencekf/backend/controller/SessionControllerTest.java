package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.GlobalExceptionHandler;
import com.presencekf.backend.service.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test d'intégration pour POST /api/sessions.
 *
 * Prouve EF2 : POST /api/sessions avec {titre, promotionId} retourne
 * 201 {id, code, ouvertureAt, expirationAt}.
 * Vérifie aussi le 400 {code, message} en cas de champ manquant (B4).
 *
 * Références : EF2, B2, B4, M1 (#15).
 */
@WebMvcTest(SessionController.class)
@Import(GlobalExceptionHandler.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Test
    void postSessions_doitRetourner201AvecChampsContractuels() throws Exception {
        LocalDateTime ouvertureAt = LocalDateTime.of(2026, 9, 27, 12, 0);
        Session session = new Session();
        session.setId(1L);
        session.setTitre("Cours de test intégration");
        session.setCode("A1B2C3");
        session.setPromotionId(1L);
        session.setOuvertureAt(ouvertureAt);
        session.setExpirationAt(ouvertureAt.plusMinutes(15));
        when(sessionService.ouvrirSession(eq("Cours de test intégration"), eq(1L)))
                .thenReturn(session);

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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("A1B2C3"))
                .andExpect(jsonPath("$.code").value(org.hamcrest.Matchers.matchesPattern("^[A-Z0-9]{6}$")))
                .andExpect(jsonPath("$.ouvertureAt").exists())
                .andExpect(jsonPath("$.expirationAt").exists());
    }

    @Test
    void postSessions_sansTitre_doitRetourner400FormatErreur() throws Exception {
        String body = """
                {
                    "promotionId": 1
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postSessions_sansPromotionId_doitRetourner400FormatErreur() throws Exception {
        String body = """
                {
                    "titre": "Cours sans promotion"
                }
                """;

        mockMvc.perform(post("/api/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
