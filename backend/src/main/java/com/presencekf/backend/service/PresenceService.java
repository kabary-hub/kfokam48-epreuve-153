package com.presencekf.backend.service;

import com.presencekf.backend.dto.PresenceDto;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    public PresenceService(PresenceRepository presenceRepository, SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    public PresenceDto marquerPresence(Long sessionId, String etudiant, String code) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        if (!session.isOuverte()) {
            throw new RuntimeException("Session fermée, impossible de marquer une présence");
        }
        LocalDateTime now = LocalDateTime.now();
        long diff = ChronoUnit.MINUTES.between(now, session.getFin());
        if (diff < 0) {
            throw new RuntimeException("Session expirée, impossible de marquer une présence");
        }
        Presence presence = new Presence(etudiant, code);
        presence.setSession(session);
        presence = presenceRepository.save(presence);
        return toDto(presence);
    }

    public PresenceDto getPresence(Long sessionId, Long id) {
        Presence presence = presenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Présence non trouvée"));
        if (!presence.getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Présence n'appartient pas à cette session");
        }
        return toDto(presence);
    }

    private PresenceDto toDto(Presence presence) {
        return new PresenceDto(presence.getId(), presence.getEtudiant(), presence.getCode(), presence.getTimestamp());
    }
}
