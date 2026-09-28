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

    /** Vérifie si une relecture existe pour un exercice. */
    boolean existsByExerciceId(Long exerciceId);

    /** Liste les relectures d'une liste d'exercices (moyenne du tableau M7). */
    List<Relecture> findByExerciceIdIn(List<Long> exerciceIds);
}
