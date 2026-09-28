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
     * Liste les exercices d'une session (utilisé par M5 pour assigner un
     * relecteur, et par M7 pour le tableau).
     */
    List<Exercice> findBySessionId(Long sessionId);
}
