package com.presencekf.backend.service;

import com.presencekf.backend.entity.Etudiant;
import com.presencekf.backend.entity.Promotion;
import com.presencekf.backend.entity.Session;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PresenceConcurrencyTest {

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
    private List<Long> etudiantIds;

    @BeforeEach
    void setUp() {
        presenceRepository.deleteAll();
        sessionRepository.deleteAll();
        etudiantRepository.deleteAll();
        promotionRepository.deleteAll();

        Promotion promotion = new Promotion();
        promotion.setNom("Promotion test concurrence");
        promotion = promotionRepository.save(promotion);

        etudiantIds = new ArrayList<>();
        for (int index = 1; index <= 10; index++) {
            Etudiant etudiant = new Etudiant();
            etudiant.setNom("Étudiant concurrence " + index);
            etudiant.setPromotionId(promotion.getId());
            etudiantIds.add(etudiantRepository.save(etudiant).getId());
        }

        Session session = new Session();
        session.setTitre("Test concurrence");
        session.setCode("RACE01");
        session.setOuvertureAt(LocalDateTime.now());
        session.setExpirationAt(LocalDateTime.now().plusMinutes(15));
        session.setPromotionId(promotion.getId());
        sessionRepository.save(session);
        code = session.getCode();
    }

    @Test
    void deuxEtudiantsSimultanes_doiventTousDeuxEtreEnregistres() throws Exception {
        int nbEtudiants = 10;
        ExecutorService executor = Executors.newFixedThreadPool(nbEtudiants);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(nbEtudiants);
        AtomicInteger succes = new AtomicInteger();

        try {
            for (Long etudiantId : etudiantIds) {
                long id = etudiantId;
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        presenceService.enregistrerPresence(code, id);
                        succes.incrementAndGet();
                    } catch (Exception ignored) {
                        // Le nombre persisté permet de vérifier le résultat de concurrence.
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            assertThat(doneLatch.await(5, TimeUnit.SECONDS))
                    .as("Les requêtes de présence doivent se terminer")
                    .isTrue();
        } finally {
            executor.shutdownNow();
        }

        long enBase = presenceRepository.count();
        System.out.println("Succès : " + succes.get() + " / " + nbEtudiants
                + " ; en base : " + enBase);
        assertThat(enBase)
                .as("Les " + nbEtudiants + " étudiants doivent être enregistrés (RG15)")
                .isEqualTo(nbEtudiants);
    }
}
