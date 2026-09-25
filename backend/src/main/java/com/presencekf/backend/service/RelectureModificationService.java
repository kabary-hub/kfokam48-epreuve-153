package com.presencekf.backend.service;

import com.presencekf.backend.dto.RelectureModificationDto;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.repository.RelectureRepository;
import org.springframework.stereotype.Service;

@Service
public class RelectureModificationService {

    private final RelectureRepository relectureRepository;

    public RelectureModificationService(RelectureRepository relectureRepository) {
        this.relectureRepository = relectureRepository;
    }

    public RelectureModificationDto modifierRelecture(Long id, String note, String commentaire) {
        Relecture relecture = relectureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relecture non trouvée"));
        relecture.setNote(note);
        relecture.setCommentaire(commentaire);
        relecture = relectureRepository.save(relecture);
        return toDto(relecture);
    }

    private RelectureModificationDto toDto(Relecture relecture) {
        return new RelectureModificationDto(relecture.getId(), relecture.getNote(), relecture.getCommentaire());
    }
}
