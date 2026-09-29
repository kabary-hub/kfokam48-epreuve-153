package com.presencekf.backend.service;

import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service d'assignation d'un relecteur à un exercice (M5, EF6).
 *
 * Exclut l'auteur et les étudiants ayant déjà un exercice à relire dans cette
 * session. Le choix est aléatoire parmi les étudiants présents éligibles.
 */
@Service
public class AssignationRelecteurService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;

    public AssignationRelecteurService(PresenceRepository presenceRepository,
                                        ExerciceRepository exerciceRepository) {
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
    }

    /**
     * Choisit un relecteur pour un exercice.
     *
     * @param sessionId la session concernée
     * @param auteurId l'étudiant qui a déposé l'exercice
     * @return l'identifiant choisi, ou vide si aucun candidat n'est disponible
     */
    public Optional<Long> choisirRelecteur(Long sessionId, Long auteurId) {
        List<Long> presents = presenceRepository.findEtudiantIdsBySessionId(sessionId);

        List<Long> candidats = presents.stream()
                .filter(id -> !id.equals(auteurId))
                .filter(id -> !exerciceRepository.existsByRelecteurIdAndSessionId(id, sessionId))
                .filter(id -> !exerciceRepository.existsByRelecteur2IdAndSessionId(id, sessionId))
                .toList();

        if (candidats.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(candidats.get(RANDOM.nextInt(candidats.size())));
    }

    /**
     * Choisit DEUX relecteurs distincts pour un exercice (M5 + issue #53).
     *
     * @param sessionId la session concernée
     * @param auteurId l'étudiant qui a déposé l'exercice
     * @return un objet avec 0, 1 ou 2 relecteurs selon les candidats disponibles
     */
    public ChoixRelecteurs choisirRelecteurs(Long sessionId, Long auteurId) {
        List<Long> presents = presenceRepository.findEtudiantIdsBySessionId(sessionId);

        List<Long> candidats = presents.stream()
                .filter(id -> !id.equals(auteurId))
                .filter(id -> !exerciceRepository.existsByRelecteurIdAndSessionId(id, sessionId))
                .filter(id -> !exerciceRepository.existsByRelecteur2IdAndSessionId(id, sessionId))
                .toList();

        // Mélanger et prendre les 2 premiers (copie mutable car List.of est immuable)
        List<Long> melanges = new ArrayList<>(candidats);
        Collections.shuffle(melanges, RANDOM);
        Long r1 = melanges.size() >= 1 ? melanges.get(0) : null;
        Long r2 = melanges.size() >= 2 ? melanges.get(1) : null;

        return new ChoixRelecteurs(r1, r2);
    }

    /**
     * Résultat de l'assignation de 2 relecteurs.
     */
    public static class ChoixRelecteurs {
        private final Long relecteur1Id;
        private final Long relecteur2Id;

        public ChoixRelecteurs(Long relecteur1Id, Long relecteur2Id) {
            this.relecteur1Id = relecteur1Id;
            this.relecteur2Id = relecteur2Id;
        }

        public Long getRelecteur1Id() { return relecteur1Id; }
        public Long getRelecteur2Id() { return relecteur2Id; }
    }
}
