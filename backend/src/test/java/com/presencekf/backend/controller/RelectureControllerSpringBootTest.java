package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelectureRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelectureControllerSpringBootTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ExerciceRepository exerciceRepository;

    @Autowired
    private RelectureRepository relectureRepository;

    private Long exerciceId;

    @BeforeEach
    void setUp() {
        relectureRepository.deleteAll();
        exerciceRepository.deleteAll();
        sessionRepository.deleteAll();

        Session session = new Session();
        session.setTitre("Cours test relecture");
        session.setCode("REL001");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(1L);
        sessionRepository.save(session);

        Exercice exercice = new Exercice();
        exercice.setSessionId(session.getId());
        exercice.setEtudiantId(1L);
        exercice.setLien("https://exemple.com/exercice");
        exercice.setStatut("EN_ATTENTE");
        exercice.setRelecteurId(2L);
        exercice.setRelecteur2Id(3L);
        exercice.setDeposeAt(LocalDateTime.now());
        exerciceRepository.save(exercice);
        exerciceId = exercice.getId();
    }

    @Test
    void postRelectures_doitRetourner200AvecStatutRelue() throws Exception {
        String body = """
                {
                    "note": 15,
                    "commentaire": "Travail correct"
                }
                """;

        mockMvc.perform(post("/api/relectures/" + exerciceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .param("relecteurId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("RELUE"))
                .andExpect(jsonPath("$.exerciceId").value(exerciceId));
    }

    @Test
    void postRelectures_noteHorsLimites_doitRetourner400NoteInvalide() throws Exception {
        String body = """
                {
                    "note": 25,
                    "commentaire": "Trop haut"
                }
                """;

        mockMvc.perform(post("/api/relectures/" + exerciceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .param("relecteurId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOTE_INVALIDE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void postRelectures_exerciceInconnu_doitRetourner404() throws Exception {
        String body = """
                {
                    "note": 15,
                    "commentaire": "OK"
                }
                """;

        mockMvc.perform(post("/api/relectures/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .param("relecteurId", "2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postRelectures_dejaRendue_doitRetourner409() throws Exception {
        String body = """
                {
                    "note": 15,
                    "commentaire": "OK"
                }
                """;

        mockMvc.perform(post("/api/relectures/" + exerciceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .param("relecteurId", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/relectures/" + exerciceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .param("relecteurId", "2"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("RELECTURE_DEJA_RENDUE"))
                .andExpect(jsonPath("$.message").exists());
    }
}
