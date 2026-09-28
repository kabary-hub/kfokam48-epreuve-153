package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour les présences.
 *
 * RG15 : une seule présence par (sessionId, etudiantId).
 * La contrainte est aussi dans la migration V1 et dans l'entité.
 */
@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {

    /**
     * Vérifie si une présence existe pour un étudiant dans une session.
     * Utilisé par M2 pour la règle RG15 (unicité).
     */
    Optional<Presence> findBySessionIdAndEtudiantId(Long sessionId, Long etudiantId);

    /**
     * Compte les présences d'une session (utilisé pour le tableau M7).
     */
    long countBySessionId(Long sessionId);
}
