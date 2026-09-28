package com.presencekf.backend.service;

import com.presencekf.backend.dto.TableauDto;
import com.presencekf.backend.entity.Etudiant;
import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.exception.PromotionInconnueException;
import com.presencekf.backend.repository.EtudiantRepository;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.PresenceRepository;
import com.presencekf.backend.repository.PromotionRepository;
import com.presencekf.backend.repository.RelectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service du tableau récapitulatif du formateur (M7, EF9).
 * La moyenne des notes est calculée côté API conformément à F3.
 */
@Service
public class TableauService {

    private final PromotionRepository promotionRepository;
    private final EtudiantRepository etudiantRepository;
    private final PresenceRepository presenceRepository;
    private final ExerciceRepository exerciceRepository;
    private final RelectureRepository relectureRepository;

    public TableauService(PromotionRepository promotionRepository,
                          EtudiantRepository etudiantRepository,
                          PresenceRepository presenceRepository,
                          ExerciceRepository exerciceRepository,
                          RelectureRepository relectureRepository) {
        this.promotionRepository = promotionRepository;
        this.etudiantRepository = etudiantRepository;
        this.presenceRepository = presenceRepository;
        this.exerciceRepository = exerciceRepository;
        this.relectureRepository = relectureRepository;
    }

    /** Construit une ligne par étudiant de la promotion demandée. */
    public List<TableauDto> construire(Long promotionId) {
        promotionRepository.findById(promotionId)
                .orElseThrow(() -> new PromotionInconnueException(
                        "Aucune promotion ne correspond à cet identifiant."));

        return etudiantRepository.findByPromotionId(promotionId).stream()
                .map(this::construireLigne)
                .toList();
    }

    private TableauDto construireLigne(Etudiant etudiant) {
        Long etudiantId = etudiant.getId();
        List<Exercice> exercices = exerciceRepository.findByEtudiantId(etudiantId);

        return new TableauDto(
                etudiantId,
                etudiant.getNom(),
                presenceRepository.countByEtudiantId(etudiantId),
                exercices.size(),
                calculerMoyenne(exercices),
                exerciceRepository.countByRelecteurIdAndStatutNot(etudiantId, "RELUE")
        );
    }

    /** Retourne null lorsqu'aucune note de relecture n'est disponible. */
    private Double calculerMoyenne(List<Exercice> exercices) {
        if (exercices.isEmpty()) {
            return null;
        }

        List<Long> exerciceIds = exercices.stream()
                .map(Exercice::getId)
                .toList();
        List<Integer> notes = relectureRepository.findByExerciceIdIn(exerciceIds).stream()
                .filter(relecture -> "RELUE".equals(relecture.getStatut()))
                .map(Relecture::getNote)
                .filter(note -> note != null)
                .toList();

        if (notes.isEmpty()) {
            return null;
        }

        return notes.stream().mapToInt(Integer::intValue).average().orElseThrow();
    }
}
