package com.presencekf.backend.service;

import com.presencekf.backend.dto.ManualPresenceDto;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ManualPresenceService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    public ManualPresenceService(PresenceRepository presenceRepository, SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    public ManualPresenceDto ajouterManuel(Long sessionId, String etudiant, String code) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        if (!session.isOuverte()) {
            throw new RuntimeException("Session fermée, impossible d'ajouter une présence manuellement");
        }
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.MINUTES.between(now, session.getFin());
        if (diff < 0) {
            throw new RuntimeException("Session expirée, impossible d'ajouter une présence manuellement");
        }
        Presence presence = new Presence(etudiant, code);
        presence.setSession(session);
        presence = presenceRepository.save(presence);
        return toDto(presence);
    }

    private ManualPresenceDto toDto(Presence presence) {
        return new ManualPresenceDto(presence.getId(), presence.getEtudiant(), presence.getCode(), presence.getTimestamp());
    }
}
