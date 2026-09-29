package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour les exercices.
 *
 * RG16 : unicité (sessionId, etudiantId).
 * M5 : utilisé pour trouver des exercices sans relecteur.
 */
@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {

    /**
     * Vérifie si un exercice existe pour un étudiant dans une session (RG16).
     */
    Optional<Exercice> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    /**
     * Vérifie si un étudiant a déjà un exercice à relire dans une session.
     */
    boolean existsByRelecteurIdAndSessionId(Long relecteurId, Long sessionId);

    /**
     * Vérifie si un étudiant est déjà affecté comme 2e relecteur dans cette
     * session (issue #53).
     */
    boolean existsByRelecteur2IdAndSessionId(Long relecteur2Id, Long sessionId);

    /** Compte les exercices déposés par un étudiant (M7). */
    long countByEtudiantId(Long etudiantId);

    /**
     * Compte les exercices assignés à un relecteur et pas encore relus (M7).
     */
    long countByRelecteurIdAndStatutNot(Long relecteurId, String statut);

    /** Liste les exercices déposés par un étudiant (pour le calcul de moyenne M7). */
    List<Exercice> findByEtudiantId(Long etudiantId);

    /**
     * Liste les exercices d'une session (utilisé par M5 pour assigner un
     * relecteur, et par M7 pour le tableau).
     */
    List<Exercice> findBySessionId(Long sessionId);
}
