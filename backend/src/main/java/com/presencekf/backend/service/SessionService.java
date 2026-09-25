package com.presencekf.backend.service;

import com.presencekf.backend.dto.PresenceDto;
import com.presencekf.backend.dto.SessionDto;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PresenceRepository presenceRepository;
    private static final long CODE_VALIDITY_MINUTES = 15;

    public SessionService(SessionRepository sessionRepository, PresenceRepository presenceRepository) {
        this.sessionRepository = sessionRepository;
        this.presenceRepository = presenceRepository;
    }

    public SessionDto ouvrirSession(String code, LocalDateTime debut, LocalDateTime fin) {
        Session session = new Session(code, debut, fin);
        session.setOuverte(true);
        session = sessionRepository.save(session);
        return toDto(session);
    }

    public SessionDto clôturerSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        if (!session.isOuverte()) {
            throw new RuntimeException("Session déjà fermée");
        }
        session.fermer();
        session = sessionRepository.save(session);
        return toDto(session);
    }

    public SessionDto marquerPresence(String code, String etudiant) {
        Session session = sessionRepository.findByCode(code)
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
        session.ajouterPresence(presence);
        presence = presenceRepository.save(presence);
        return toDto(session);
    }

    public SessionDto getSessionByCode(String code) {
        Session session = sessionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        return toDto(session);
    }

    public List<SessionDto> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SessionDto toDto(Session session) {
        SessionDto dto = new SessionDto(session.getId(), session.getCode(), session.getDebut(), session.getFin(), session.isOuverte());
        dto.setPresences(session.getPresences().stream().map(p -> {
            PresenceDto pd = new PresenceDto(p.getId(), p.getEtudiant(), p.getCode(), p.getTimestamp());
            return pd;
        }).collect(Collectors.toList()));
        dto.setExercices(session.getExercices().stream().map(e -> {
            ExerciceDto ed = new ExerciceDto(e.getId(), e.getLien(), e.getEtudiant());
            return ed;
        }).collect(Collectors.toList()));
        return dto;
    }
}
