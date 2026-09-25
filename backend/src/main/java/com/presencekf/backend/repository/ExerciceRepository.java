package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
}
