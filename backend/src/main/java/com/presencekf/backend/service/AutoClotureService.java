package com.presencekf.backend.service;

import com.presencekf.backend.dto.AutoClotureDto;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AutoClotureService {

    private final SessionRepository sessionRepository;

    public AutoClotureService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public AutoClotureDto autoClôturer(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        if (!session.isOuverte()) {
            throw new RuntimeException("Session déjà fermée");
        }
        session.fermer();
        session = sessionRepository.save(session);
        return toDto(session);
    }

    public AutoClotureDto getStatus(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        boolean expirée = !session.isOuverte() && LocalDateTime.now().isAfter(session.getFin());
        return toDto(session, expirée);
    }

    private AutoClotureDto toDto(Session session) {
        return toDto(session, !session.isOuverte() && LocalDateTime.now().isAfter(session.getFin()));
    }

    private AutoClotureDto toDto(Session session, boolean expirée) {
        return new AutoClotureDto(session.getId(), session.getCode(), session.isOuverte(), session.getFin(), expirée);
    }
}
