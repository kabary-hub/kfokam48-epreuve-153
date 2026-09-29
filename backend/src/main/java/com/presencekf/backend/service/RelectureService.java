package com.presencekf.backend.service;

import com.presencekf.backend.entity.Exercice;
import com.presencekf.backend.entity.Relecture;
import com.presencekf.backend.entity.Session;
import com.presencekf.backend.exception.AutoRelectureException;
import com.presencekf.backend.exception.NoteInvalideException;
import com.presencekf.backend.exception.RelectureDejaRendueException;
import com.presencekf.backend.exception.SessionClotureeException;
import com.presencekf.backend.exception.SessionInconnueException;
import com.presencekf.backend.repository.ExerciceRepository;
import com.presencekf.backend.repository.RelectureRepository;
import com.presencekf.backend.repository.SessionRepository;
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
    private final SessionRepository sessionRepository;

    public RelectureService(RelectureRepository relectureRepository,
                            ExerciceRepository exerciceRepository,
                            SessionRepository sessionRepository) {
        this.relectureRepository = relectureRepository;
        this.exerciceRepository = exerciceRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Enregistre une relecture pour un exercice (M6, EF7, issue #53).
     *
     * @param exerciceId identifiant de l'exercice à relire
     * @param relecteurId identifiant du relecteur qui rend la note
     * @param note note entière entre 0 et 20
     * @param commentaire commentaire (éventuellement nul)
     * @return la relecture créée ou mise à jour
     */
    @Transactional
    public Relecture rendre(Long exerciceId, Long relecteurId,
                           Integer note, String commentaire) {
        Exercice exercice = exerciceRepository.findById(exerciceId)
                .orElseThrow(() -> new SessionInconnueException(
                        "Aucun exercice ne correspond à cet identifiant."));

        // Vérifier que la session n'est pas clôturée (M8, RG14)
        Session session = sessionRepository.findById(exercice.getSessionId())
                .orElseThrow(() -> new SessionInconnueException(
                        "Session de l'exercice introuvable."));
        if (session.getClotureAt() != null) {
            throw new SessionClotureeException(
                    "La session est clôturée, aucune relecture n'est possible.");
        }

        // Vérifier que le relecteur est bien assigné (1er ou 2e relecteur)
        if (exercice.getRelecteurId() == null
                || (!relecteurId.equals(exercice.getRelecteurId())
                    && !relecteurId.equals(exercice.getRelecteur2Id()))) {
            throw new RelecteurNonAssigneException(
                    "Cet étudiant n'est pas relecteur de cet exercice.");
        }

        // Vérifier que le relecteur n'est pas l'auteur (Q5, RG4)
        if (relecteurId.equals(exercice.getEtudiantId())) {
            throw new AutoRelectureException(
                    "Un étudiant ne peut pas relire son propre exercice.");
        }

        // Vérifier si le relecteur a déjà rendu sa relecture
        Optional<Relecture> existing =
                relectureRepository.findByExerciceIdAndRelecteurId(exerciceId,
                        relecteurId);
        if (existing.isPresent() && "RELUE".equals(existing.get().getStatut())) {
            // Une relecture rendue est définitive (M8, RG14).
            throw new RelectureDejaRendueException(
                    "Vous avez déjà rendu cette relecture.");
        }

        if (note == null || note < 0 || note > 20) {
            throw new NoteInvalideException(
                    "La note doit être un entier entre 0 et 20.");
        }

        // Créer ou mettre à jour la relecture
        Relecture relecture = existing.orElseGet(() -> {
            Relecture r = new Relecture();
            r.setExerciceId(exerciceId);
            r.setRelecteurId(relecteurId);
            return r;
        });
        relecture.setNote(note);
        relecture.setCommentaire(commentaire);
        relecture.setStatut("RELUE");
        relecture.setRendueAt(LocalDateTime.now());
        Relecture savedRelecture = relectureRepository.save(relecture);

        // Calculer le statut de l'exercice (PROVISOIRE ou RELUE)
        long nbRendues =
                relectureRepository.countByExerciceIdAndStatut(exerciceId,
                        "RELUE");
        if (nbRendues >= 2) {
            exercice.setStatut("RELUE");
        } else {
            exercice.setStatut("PROVISOIRE");
        }
        exerciceRepository.save(exercice);

        return savedRelecture;
    }
}
