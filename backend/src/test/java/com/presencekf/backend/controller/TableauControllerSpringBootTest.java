package com.presencekf.backend.controller;

import com.presencekf.backend.entity.Etudiant;
import com.presencekf.backend.entity.Promotion;
import com.presencekf.backend.repository.EtudiantRepository;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.PromotionRepository;
import com.presencekf.backend.repository.RelectureRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TableauControllerSpringBootTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PromotionRepository promotionRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private PresenceRepository presenceRepository;
    @Autowired private ExerciceRepository exerciceRepository;
    @Autowired private RelectureRepository relectureRepository;
    @Autowired private SessionRepository sessionRepository;

    private Long promotionId;

    @BeforeEach
    void setUp() {
        relectureRepository.deleteAll();
        exerciceRepository.deleteAll();
        presenceRepository.deleteAll();
        etudiantRepository.deleteAll();
        sessionRepository.deleteAll();
        promotionRepository.deleteAll();

        Promotion promotion = new Promotion();
        promotion.setNom("Promo test");
        promotionRepository.save(promotion);
        promotionId = promotion.getId();

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Alice");
        etudiant.setPromotionId(promotionId);
        etudiantRepository.save(etudiant);
    }

    @Test
    void getTableau_doitRetourner200AvecLesIndicateursDeLEtudiant() throws Exception {
        mockMvc.perform(get("/api/tableau").param("promotionId", promotionId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].etudiantId").exists())
                .andExpect(jsonPath("$[0].nom").value("Alice"))
                .andExpect(jsonPath("$[0].presences").value(0))
                .andExpect(jsonPath("$[0].exercicesDeposes").value(0))
                .andExpect(jsonPath("$[0].moyenne").value((Object) null))
                .andExpect(jsonPath("$[0].relecturesEnAttente").value(0));
    }

    @Test
    void getTableau_promotionInconnue_doitRetourner404() throws Exception {
        mockMvc.perform(get("/api/tableau").param("promotionId", "99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("PROMOTION_INCONNUE"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getTableau_sansPromotionId_doitRetourner400() throws Exception {
        mockMvc.perform(get("/api/tableau"))
                .andExpect(status().isBadRequest());
    }
}
