package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.ExerciceDejaDeposeException;
import com.presencekf.backend.exception.LienInvalideException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

/**
 * Service métier pour les exercices.
 *
 * Règles couvertes :
 * - EF4 : dépôt du lien d'un exercice
 * - RG16 : unicité (1 exercice par étudiant et par session)
 *
 * Le statut initial est EN_ATTENTE. L'assignation d'un relecteur est faite
 * par M5 (AssignationRelecteurService), appelée séparément.
 */
@Service
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;

    public ExerciceService(ExerciceRepository exerciceRepository,
                           SessionRepository sessionRepository) {
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Dépose un exercice pour un étudiant dans une session.
     *
     * @param sessionId  l'identifiant de la session
     * @param etudiantId l'identifiant de l'étudiant
     * @param lien       l'URL de l'exercice
     * @return l'Exercice créé
     * @throws SessionInconnueException     si la session n'existe pas
     * @throws LienInvalideException        si le lien n'est pas une URL http(s) valide
     * @throws ExerciceDejaDeposeException  si un exercice existe déjà (RG16)
     */
    public Exercice deposer(Long sessionId, Long etudiantId, String lien) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionInconnueException(
                        "Aucune session ne correspond à cet identifiant."));

        if (session.getClotureAt() != null) {
            throw new ExerciceDejaDeposeException(
                    "La session est clôturée, aucun dépôt n'est possible.");
        }

        validerLien(lien);

        exerciceRepository.findBySessionIdAndEtudiantId(sessionId, etudiantId)
                .ifPresent(exercice -> {
                    throw new ExerciceDejaDeposeException(
                            "Un exercice a déjà été déposé pour cet étudiant sur cette session.");
                });

        Exercice exercice = new Exercice();
        exercice.setSessionId(sessionId);
        exercice.setEtudiantId(etudiantId);
        exercice.setLien(lien);
        exercice.setStatut("EN_ATTENTE");
        exercice.setDeposeAt(LocalDateTime.now());

        return exerciceRepository.save(exercice);
    }

    /** Valide qu'un lien est une URL http(s) bien formée avec un hôte. */
    private void validerLien(String lien) {
        if (lien == null || lien.isBlank()) {
            throw new LienInvalideException("Le lien est obligatoire.");
        }

        try {
            URI uri = new URI(lien);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http")
                    && !scheme.equalsIgnoreCase("https"))) {
                throw new LienInvalideException(
                        "Le lien doit être une URL http(s) valide.");
            }
            if (uri.getHost() == null) {
                throw new LienInvalideException(
                        "Le lien doit contenir un nom d'hôte.");
            }
        } catch (URISyntaxException exception) {
            throw new LienInvalideException("Le lien est mal formé.");
        }
    }
}
