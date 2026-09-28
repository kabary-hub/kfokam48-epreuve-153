package com.presencekf.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

/**
 * Entité Presence, mappée sur la table `presences` de la migration V1.
 *
 * Champs conformes à V1 :
 * - id, sessionId, etudiantId, source, marqueAt
 *
 * Contrainte d'unicité : (sessionId, etudiantId) → RG15.
 * Source : ETUDIANT (marquage par code) ou FORMATEUR (ajout manuel Q14).
 */
@Entity
@Table(
    name = "presences",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_presence_session_etudiant",
        columnNames = {"session_id", "etudiant_id"}
    )
)
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(name = "marque_at", nullable = false)
    private LocalDateTime marqueAt;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getMarqueAt() { return marqueAt; }
    public void setMarqueAt(LocalDateTime marqueAt) { this.marqueAt = marqueAt; }
}
