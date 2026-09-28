package com.presencekf.backend.service;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.CodeExpireException;
import com.presencekf.backend.exception.CodeInconnuException;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service métier pour les présences.
 *
 * Règles couvertes :
 * - RG1 : code expiré après 15 minutes (Q2)
 * - RG2 : pas de présence après expiration (Q3)
 * - RG15 : unicité (1 présence par session et par étudiant)
 * - EF1 : marquage de présence par code
 *
 * La règle RG3 (5 erreurs → 2 min blocage) sera ajoutée dans une
 * itération ultérieure si le temps le permet (Q4).
 */
@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    public PresenceService(PresenceRepository presenceRepository,
                           SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Enregistre une présence à partir d'un code de session.
     *
     * @param code       le code de présence
     * @param etudiantId l'identifiant de l'étudiant
     * @return la Presence créée
     * @throws CodeInconnuException si le code ne correspond à aucune session
     * @throws CodeExpireException  si le code a expiré (RG1)
     * @throws DejaPresentException si l'étudiant est déjà présent (RG15)
     */
    public Presence enregistrerPresence(String code, Long etudiantId) {
        // 1. Trouver la session par son code
        Session session = sessionRepository.findByCode(code)
                .orElseThrow(() -> new CodeInconnuException(
                        "Aucune session ne correspond à ce code."));

        // 2. Vérifier l'expiration (RG1 + RG2)
        if (LocalDateTime.now().isAfter(session.getExpirationAt())) {
            throw new CodeExpireException("Le code de présence a expiré.");
        }

        // 3. Vérifier l'unicité (RG15)
        presenceRepository.findBySessionIdAndEtudiantId(session.getId(), etudiantId)
                .ifPresent(p -> {
                    throw new DejaPresentException(
                            "Une présence existe déjà pour cet étudiant sur cette session.");
                });

        // 4. Créer la présence
        Presence presence = new Presence();
        presence.setSessionId(session.getId());
        presence.setEtudiantId(etudiantId);
        presence.setSource("ETUDIANT");
        presence.setMarqueAt(LocalDateTime.now());

        return presenceRepository.save(presence);
    }
}
