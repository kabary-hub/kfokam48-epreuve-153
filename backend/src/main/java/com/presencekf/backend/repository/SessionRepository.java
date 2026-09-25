package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByCode(String code);
    Optional<Session> findFirstByOuverteTrueOrderByFinAsc();
}
