package com.presencekf.backend.service;

import com.presencekf.backend.dto.NoteDto;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final PresenceRepository presenceRepository;
    private final SessionRepository sessionRepository;

    public NoteService(PresenceRepository presenceRepository, SessionRepository sessionRepository) {
        this.presenceRepository = presenceRepository;
        this.sessionRepository = sessionRepository;
    }

    public NoteDto getNote(Long sessionId, String etudiant) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        Presence presence = presenceRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Présence non trouvée pour cet étudiant"));
        // Note calculée : présence + commentaire
        return new NoteDto(presence.getId(), etudiant, presence.getCode(), presence.getTimestamp(), "Commentaire de la relecture");
    }
}
