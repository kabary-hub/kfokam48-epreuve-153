package com.presencekf.backend.service;

import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.CodeExpireException;
import com.presencekf.backend.exception.CodeInconnuException;
import com.presencekf.backend.exception.DejaPresentException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.exception.TropTentativesException;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service métier pour les présences.
 *
 * Règles couvertes :
 * - RG1 : code expiré après 15 minutes (Q2)
 * - RG2 : pas de présence après expiration (Q3)
 * - RG3 : 5 tentatives erronées → blocage 2 min (Q4)
 * - RG15 : unicité (1 présence par session et par étudiant)
 * - EF1 : marquage de présence par code
 *
 * RG3 est stocké en mémoire (ConcurrentHashMap). Cette approche convient
 * à l'épreuve (pas de persistance requise, redémarrage réinitialise).
 */
@Service
public class PresenceService {

    private static final int MAX_TENTATIVES = 5;
    private static final int BLOCAGE_MINUTES = 2;

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    /** Compteur de tentatives erronées par clé "etudiantId:sessionId". */
    private final Map<String, Integer> tentatives = new ConcurrentHashMap<>();

    /** Timestamp de fin de blocage par clé "etudiantId:sessionId". */
    private final Map<String, LocalDateTime> blocages = new ConcurrentHashMap<>();

    public PresenceService(PresenceRepository presenceRepository,
                           SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Enregistre une présence à partir d'un code de session.
     */
    @Transactional
    public Presence enregistrerPresence(String code, Long etudiantId) {
        verifierBlocage(etudiantId);

        Session session = sessionRepository.findByCode(code)
                .orElseThrow(() -> {
                    // Sans session trouvée, l'erreur est suivie au niveau étudiant.
                    enregistrerTentative(etudiantId, null);
                    return new CodeInconnuException(
                            "Aucune session ne correspond à ce code.");
                });

        Long sessionId = session.getId();
        if (LocalDateTime.now().isAfter(session.getExpirationAt())) {
            throw new CodeExpireException("Le code de présence a expiré.");
        }

        presenceRepository.findBySessionIdAndEtudiantId(sessionId, etudiantId)
                .ifPresent(p -> {
                    throw new DejaPresentException(
                            "Une présence existe déjà pour cet étudiant sur cette session.");
                });

        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource("ETUDIANT");
        presence.setMarqueAt(LocalDateTime.now());

        try {
            Presence saved = presenceRepository.saveAndFlush(presence);
            reinitialiserTentatives(etudiantId);
            return saved;
        } catch (DataIntegrityViolationException exception) {
            throw new DejaPresentException(
                    "Une présence existe déjà pour cet étudiant sur cette session.");
        }
    }

    /**
     * Enregistre une présence ajoutée manuellement par le formateur (M3, EF3).
     *
     * La présence porte source = "FORMATEUR" (RG13, Q14).
     *
     * @param sessionId  l'identifiant de la session
     * @param etudiantId l'identifiant de l'étudiant
     * @return la Presence créée
     * @throws SessionInconnueException si la session n'existe pas
     * @throws DejaPresentException    si l'étudiant est déjà présent (RG15)
     */
    @Transactional
    public Presence enregistrerPresenceFormateur(Long sessionId, Long etudiantId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionInconnueException(
                        "Aucune session ne correspond à cet identifiant."));

        presenceRepository.findBySessionIdAndEtudiantId(sessionId, etudiantId)
                .ifPresent(p -> {
                    throw new DejaPresentException(
                            "Une présence existe déjà pour cet étudiant sur cette session.");
                });

        Presence presence = new Presence();
        presence.setSessionId(sessionId);
        presence.setEtudiantId(etudiantId);
        presence.setSource("FORMATEUR");
        presence.setMarqueAt(LocalDateTime.now());

        try {
            return presenceRepository.saveAndFlush(presence);
        } catch (DataIntegrityViolationException exception) {
            throw new DejaPresentException(
                    "Une présence existe déjà pour cet étudiant sur cette session.");
        }
    }

    /** Vérifie et retire les blocages expirés associés à l'étudiant. */
    private void verifierBlocage(Long etudiantId) {
        String prefix = etudiantId + ":";
        LocalDateTime now = LocalDateTime.now();
        boolean bloque = false;

        for (Map.Entry<String, LocalDateTime> entry : blocages.entrySet()) {
            if (!entry.getKey().startsWith(prefix)) {
                continue;
            }

            if (now.isAfter(entry.getValue())) {
                blocages.remove(entry.getKey(), entry.getValue());
                tentatives.remove(entry.getKey());
            } else {
                bloque = true;
            }
        }

        if (bloque) {
            throw new TropTentativesException(
                    "Trop de tentatives. Réessayez dans " + BLOCAGE_MINUTES + " minutes.");
        }
    }

    /** Enregistre une tentative erronée et bloque dès la cinquième erreur. */
    private void enregistrerTentative(Long etudiantId, Long sessionId) {
        String key = cle(etudiantId, sessionId);
        int count = tentatives.merge(key, 1, Integer::sum);
        if (count >= MAX_TENTATIVES) {
            blocages.put(key, LocalDateTime.now().plusMinutes(BLOCAGE_MINUTES));
            tentatives.remove(key);
        }
    }

    private void reinitialiserTentatives(Long etudiantId) {
        String prefix = etudiantId + ":";
        tentatives.keySet().removeIf(key -> key.startsWith(prefix));
        blocages.keySet().removeIf(key -> key.startsWith(prefix));
    }

    private String cle(Long etudiantId, Long sessionId) {
        return etudiantId + ":" + sessionId;
    }
}
