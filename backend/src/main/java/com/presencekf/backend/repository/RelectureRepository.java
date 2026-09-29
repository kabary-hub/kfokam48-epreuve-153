package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Relecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelectureRepository extends JpaRepository<Relecture, Long> {

    /** Trouve la relecture associée à un exercice (relation 1-1). */
    Optional<Relecture> findByExerciceId(Long exerciceId);

    /**
     * Trouve la relecture d'un exercice rendue par un relecteur donné
     * (issue #53 : 2 relectures possibles par exercice).
     */
    Optional<Relecture> findByExerciceIdAndRelecteurId(
            Long exerciceId, Long relecteurId);

    /**
     * Compte les relectures RELUE pour un exercice donné
     * (issue #53 : calcul du statut PROVISOIRE/RELUE).
     */
    long countByExerciceIdAndStatut(Long exerciceId, String statut);

    /** Vérifie si une relecture existe pour un exercice. */
    boolean existsByExerciceId(Long exerciceId);

    /** Liste les relectures d'une liste d'exercices (moyenne du tableau M7). */
    List<Relecture> findByExerciceIdIn(List<Long> exerciceIds);
}
