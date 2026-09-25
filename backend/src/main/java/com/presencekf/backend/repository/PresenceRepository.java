package com.presencekf.backend.repository;

import com.presencekf.backend.entity.Presence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {
}
