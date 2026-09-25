package com.presencekf.backend.service;

import com.presencekf.backend.dto.TableauDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Presence;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableauService {

    private final SessionRepository sessionRepository;
    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;

    public TableauService(SessionRepository sessionRepository,
                         PresenceRepository presenceRepository,
                         ExerciceRepository exerciceRepository) {
        this.sessionRepository = sessionRepository;
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
    }

    public List<TableauDto> getTableau() {
        return sessionRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TableauDto getTableauBySession(String code) {
        Session session = sessionRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        return toDto(session);
    }

    private TableauDto toDto(Session session) {
        List<PresenceDto> presences = session.getPresences().stream()
                .map(p -> new PresenceDto(p.getId(), p.getEtudiant(), p.getCode(), p.getTimestamp()))
                .collect(Collectors.toList());
        List<ExerciceDto> exercices = session.getExercices().stream()
                .map(e -> new ExerciceDto(e.getId(), e.getLien(), e.getEtudiant()))
                .collect(Collectors.toList());
        return new TableauDto(session.getId(), session.getCode(), session.isOuverte(), presences, exercices);
    }
}
