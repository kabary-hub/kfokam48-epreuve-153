package com.presencekf.backend.service;

import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
                .toList();

        if (candidats.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(candidats.get(RANDOM.nextInt(candidats.size())));
    }
}
