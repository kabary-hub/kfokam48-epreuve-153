package com.presencekf.backend.service;

import com.presencekf.backend.dto.RelecteurDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecteur;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelecteurRepository;
import com.presencekf.backend.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class RelecteurService {

    private final RelecteurRepository relecteurRepository;
    private final ExerciceRepository exerciceRepository;
    private final SessionRepository sessionRepository;

    public RelecteurService(RelecteurRepository relecteurRepository,
                           ExerciceRepository exerciceRepository,
                           SessionRepository sessionRepository) {
        this.relecteurRepository = relecteurRepository;
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    public RelecteurDto affecterRelecteur(Long sessionId, String etudiant, Long exerciceId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));
        if (!exercice.getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Exercice n'appartient pas à cette session");
        }
        Relecteur relecteur = new Relecteur(etudiant, exercice);
        relecteur = relecteurRepository.save(relecteur);
        return toDto(relecteur);
    }

    public RelecteurDto getRelecteur(Long sessionId, Long id) {
        Relecteur relecteur = relecteurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relecteur non trouvé"));
        if (!relecteur.getExercice().getSession().getId().equals(sessionId)) {
            throw new RuntimeException("Relecteur n'appartient pas à cette session");
        }
        return toDto(relecteur);
    }

    private RelecteurDto toDto(Relecteur relecteur) {
        return new RelecteurDto(relecteur.getId(), relecteur.getEtudiant(), relecteur.getExercice().getId());
    }
}
