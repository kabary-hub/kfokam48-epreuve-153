package com.presencekf.backend.service;

import com.presencekf.backend.entity.Etudiant;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Promotion;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.repository.EtudiantRepository;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.PromotionRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class PresenceServiceContrainteTest {

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    private String code;
    private Long sessionId;
    private Long etudiantId;

    @BeforeEach
    void setUp() {
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();
        etudiantRepository.deleteAll();
        promotionRepository.deleteAll();

        Promotion promotion = new Promotion();
        promotion.setNom("Promotion contrainte test");
        promotion = promotionRepository.save(promotion);

        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Étudiant contrainte test");
        etudiant.setPromotionId(promotion.getId());
        etudiantId = etudiantRepository.save(etudiant).getId();

        Session session = creerSession("CONT01", promotion.getId());
        sessionId = session.getId();
        code = session.getCode();
    }

    @Test
    void presenceExistante_doitEtreSignaleeCommeDejaPresente() {
        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource("ETUDIANT");
        presence.setMarqueAt(LocalDateTime.now());
        presenceRepository.save(presence);

        assertThatThrownBy(() -> presenceService.enregistrerPresence(code, etudiantId))
                .isInstanceOf(DejaPresentException.class);
    }

    @Test
    void countByEtudiantId_compteLesPresencesDeToutesLesSessions() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNom("Étudiant seconde session");
        etudiant.setPromotionId(etudiantRepository.findAll().get(0).getPromotionId());
        Long secondEtudiantId = etudiantRepository.save(etudiant).getId();
        Session autreSession = creerSession("CONT02", etudiant.getPromotionId());
        presenceRepository.save(creerPresence(sessionId, secondEtudiantId));
        presenceRepository.save(creerPresence(autreSession.getId(), secondEtudiantId));

        assertThat(presenceRepository.countByEtudiantId(secondEtudiantId)).isEqualTo(2L);
    }

    private Session creerSession(String codeSession, Long promotionId) {
        Session session = new Session();
        session.setTitre("Test contrainte");
        session.setCode(codeSession);
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(promotionId);
        return sessionRepository.save(session);
    }

    private Presence creerPresence(Long sessionId, Long etudiantId) {
        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource("ETUDIANT");
        presence.setMarqueAt(LocalDateTime.now());
        return presence;
    }
}
