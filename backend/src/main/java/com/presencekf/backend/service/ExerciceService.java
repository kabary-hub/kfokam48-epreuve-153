package com.presencekf.backend.service;

import com.presencekf.backend.dto.ExerciceDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;

    public ExerciceService(ExerciceRepository exerciceRepository, SessionRepository sessionRepository) {
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    public ExerciceDto deposerExercice(Long sessionId, String lien, String etudiant) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        Exercice exercice = new Exercice(lien, etudiant);
        exercice.setSession(session);
        exercice = exerciceRepository.save(exercice);
        return toDto(exercice);
    }

    public ExerciceDto getExercice(Long sessionId, Long id) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));
        if (!exercice.getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Exercice n'appartient pas à cette session");
        }
        return toDto(exercice);
    }

    private ExerciceDto toDto(Exercice exercice) {
        return new ExerciceDto(exercice.getId(), exercice.getLien(), exercice.getEtudiant());
    }
}
