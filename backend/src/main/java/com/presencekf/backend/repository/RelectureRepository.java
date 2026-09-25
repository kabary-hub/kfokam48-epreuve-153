package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Relecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelectureRepository extends JpaRepository<Relecture, Long> {
}
