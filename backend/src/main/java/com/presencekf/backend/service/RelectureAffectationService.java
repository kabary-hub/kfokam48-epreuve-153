package com.presencekf.backend.service;

import com.presencekf.backend.dto.RelectureAffectationDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecteur;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelecteurRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelectureAffectationService {

    private final RelecteurRepository relecteurRepository;
    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;

    public RelectureAffectationService(RelecteurRepository relecteurRepository,
                                     ExerciceRepository exerciceRepository,
                                     SessionRepository sessionRepository) {
        this.relecteurRepository = relecteurRepository;
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<RelectureAffectationDto> getRelecturesAffectees(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        return relecteurRepository.findAll().stream()
                .filter(r -> r.getExercice().getSession().getId().equals(sessionId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RelectureAffectationDto toDto(Relecteur relecteur) {
        return new RelectureAffectationDto(relecteur.getId(), relecteur.getEtudiant(), relecteur.getExercice().getId(), relecteur.getExercice().getLien());
    }
}
