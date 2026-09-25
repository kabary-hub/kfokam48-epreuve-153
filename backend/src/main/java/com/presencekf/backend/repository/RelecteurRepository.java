package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Relecteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelecteurRepository extends JpaRepository<Relecteur, Long> {
}
