package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.exception.AutoRelectureException;
import com.presencekf.backend.exception.NoteInvalideException;
import com.presencekf.backend.exception.RelectureDejaRendueException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelectureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service métier pour les relectures (M6, EF7).
 *
 * Règles couvertes :
 * - RG4 : auto-relecture interdite (Q5)
 * - RG8 : note entière 0-20 (Q9)
 * - RG9 : une relecture rendue est marquée RELUE
 */
@Service
public class RelectureService {

    private final RelectureRepository relectureRepository;
    private final ExerciceRepository exerciceRepository;

    public RelectureService(RelectureRepository relectureRepository,
                            ExerciceRepository exerciceRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
    }

    /**
     * Enregistre une première relecture pour un exercice.
     *
     * @param exerciceId identifiant de l'exercice à relire
     * @param note note entière entre 0 et 20
     * @param commentaire commentaire (éventuellement nul)
     * @return la relecture créée ou mise à jour
     */
    @Transactional
    public Relecture rendre(Long exerciceId, Integer note, String commentaire) {
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new SessionInconnueException(
                        "Aucun exercice ne correspond à cet identifiant."));

        if (exercice.getRelecteurId() == null) {
            throw new AutoRelectureException(
                    "Aucun relecteur n'est assigné à cet exercice.");
        }

        if (exercice.getRelecteurId().equals(exercice.getEtudiantId())) {
            throw new AutoRelectureException(
                    "Un étudiant ne peut pas relire son propre exercice.");
        }

        Relecture relecture = relectureRepository.findByExerciceId(exerciceId)
                .orElse(null);
        if (relecture != null && "RELUE".equals(relecture.getStatut())) {
            // La modification avant clôture sera traitée en M7 (Q10 > Q15).
            throw new RelectureDejaRendueException(
                    "Une relecture a déjà été rendue pour cet exercice.");
        }

        if (note == null || note < 0 || note > 20) {
            throw new NoteInvalideException(
                    "La note doit être un entier entre 0 et 20.");
        }

        if (relecture == null) {
            relecture = new Relecture();
            relecture.setExerciceId(exerciceId);
            relecture.setRelecteurId(exercice.getRelecteurId());
        }
        relecture.setNote(note);
        relecture.setCommentaire(commentaire);
        relecture.setStatut("RELUE");
        relecture.setRendueAt(LocalDateTime.now());
        Relecture savedRelecture = relectureRepository.save(relecture);

        exercice.setStatut("RELUE");
        exerciceRepository.save(exercice);

        return savedRelecture;
    }
}
