package com.presencekf.backend.service;

import com.presencekf.backend.dto.RelectureDto;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelectureRepository;
import org.springframework.stereotype.Service;

@Service
public class RelectureService {

    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;

    public RelectureService(RelectureRepository relectureRepository, ExerciceRepository exerciceRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
    }

    public RelectureDto demarrerRelecture(Long exerciceId) {
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new RuntimeException("Exercice non trouvé"));
        Relecture relecture = new Relecture(exercice);
        relecture = relectureRepository.save(relecture);
        return toDto(relecture);
    }

    public RelectureDto rendreRelecture(Long id) {
        Relecture relecture = relectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relecture non trouvée"));
        relecture.setTerminee(true);
        relecture = relectureRepository.save(relecture);
        return toDto(relecture);
    }

    private RelectureDto toDto(Relecture relecture) {
        return new RelectureDto(relecture.getId(), relecture.getExercice().getId(), relecture.isTerminee());
    }
}
