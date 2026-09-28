package com.presencekf.backend.service;

import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.SessionDejaClotureeException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Service métier pour les sessions de cours.
 *
 * Règles couvertes :
 * - RG1 : un code de présence expire 15 minutes après l'ouverture (Q2).
 * - EF2 : ouverture d'une session à partir de {titre, promotionId}.
 * - B3 : logique métier isolée du contrôleur et du repository.
 */
@Service
public class SessionService {

    /** RG1 : durée de validité du code en minutes (Q2). */
    private static final int CODE_VALIDITY_MINUTES = 15;

    /** Alphabet du code : majuscules et chiffres. */
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /** Longueur du code de présence. */
    private static final int CODE_LENGTH = 6;

    /** Générateur de nombres aléatoires sécurisé (non devinable, Q4). */
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Ouvre une nouvelle session de cours.
     *
     * @param titre       le titre de la session
     * @param promotionId l'identifiant de la promotion
     * @return la Session créée avec son code et ses horaires calculés
     */
    public Session ouvrirSession(String titre, Long promotionId) {
        Session session = new Session();
        session.setTitre(titre);
        session.setCode(genererCode());
        session.setPromotionId(promotionId);

        LocalDateTime now = LocalDateTime.now();
        session.setOuvertureAt(now);
        session.setExpirationAt(now.plusMinutes(CODE_VALIDITY_MINUTES));

        return sessionRepository.save(session);
    }

    /**
     * Clôture une session (M8, EF11).
     *
     * Après clôture, aucun dépôt d'exercice ni modification de relecture
     * n'est possible (RG11, RG14, Q12).
     *
     * @param sessionId l'identifiant de la session
     * @return la Session clôturée
     * @throws SessionInconnueException si la session n'existe pas
     * @throws SessionDejaClotureeException si la session est déjà clôturée
     */
    public Session cloturer(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionInconnueException(
                        "Aucune session ne correspond à cet identifiant."));

        if (session.getClotureAt() != null) {
            throw new SessionDejaClotureeException(
                    "Cette session est déjà clôturée.");
        }

        session.setClotureAt(LocalDateTime.now());
        return sessionRepository.save(session);
    }

    /**
     * Génère un code aléatoire de 6 caractères [A-Z0-9].
     * Utilise SecureRandom pour éviter la devinette entre étudiants (Q4).
     */
    private String genererCode() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
