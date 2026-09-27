package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour les sessions.
 *
 * M1 : findByCode (utilisé par M2 pour trouver la session par code).
 * La logique métier est dans SessionService (B3).
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Recherche une session par son code de présence.
     * Utilisé par M2 (marquer la présence avec un code).
     */
    Optional<Session> findByCode(String code);
}
