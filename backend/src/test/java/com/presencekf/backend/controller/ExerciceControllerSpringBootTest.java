package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
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
class ExerciceControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private ExerciceRepository exerciceRepository;

    private Long sessionId;

    @BeforeEach
    void setUp() {
        exerciceRepository.deleteAll();
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setTitre("Cours test exercice");
        session.setCode("EX0001");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(1L);
        sessionRepository.save(session);
        sessionId = session.getId();

        marquerPresent(1L);
        marquerPresent(2L);
    }

    private void marquerPresent(Long etudiantId) {
        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource("FORMATEUR");
        presence.setMarqueAt(LocalDateTime.now());
        presenceRepository.save(presence);
    }

    @Test
    void postExercices_doitRetourner201AvecStatutEnAttente() throws Exception {
        String body = """
                {
                    "sessionId": %d,
                    "etudiantId": 1,
                    "lien": "https://exemple.com/exercice1"
                }
                """.formatted(sessionId);

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE"));
    }

    @Test
    void postExercices_sansCandidat_doitRetournerStatutEnAttenteSansRelecteur() throws Exception {
        presenceRepository.deleteAll();

        String body = """
                {
                    "sessionId": %d,
                    "etudiantId": 1,
                    "lien": "https://exemple.com/exercice-sans-relecteur"
                }
                """.formatted(sessionId);

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statut").value("EN_ATTENTE_SANS_RELECTEUR"));
    }

    @Test
    void postExercices_lienInvalide_doitRetourner400() throws Exception {
        String body = """
                {
                    "sessionId": %d,
                    "etudiantId": 1,
                    "lien": "pas-une-url"
                }
                """.formatted(sessionId);

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("LIEN_INVALIDE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postExercices_dejaDepose_doitRetourner409() throws Exception {
        String body = """
                {
                    "sessionId": %d,
                    "etudiantId": 1,
                    "lien": "https://exemple.com/exercice1"
                }
                """.formatted(sessionId);

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EXERCICE_DEJA_DEPOSE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postExercices_sessionInconnue_doitRetourner404() throws Exception {
        String body = """
                {
                    "sessionId": 999,
                    "etudiantId": 1,
                    "lien": "https://exemple.com/exercice1"
                }
                """;

        mockMvc.perform(post("/api/exercices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SESSION_INCONNUE"))
                .andExpect(jsonPath("$.message").exists());
    }
}
