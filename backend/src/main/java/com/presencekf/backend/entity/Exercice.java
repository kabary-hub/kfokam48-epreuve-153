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
 * Entité Exercice, mappée sur la table `exercices` de la migration V1.
 *
 * Champs :
 * - id, sessionId, etudiantId, lien, statut, relecteurId (nullable), deposeAt
 *
 * Contrainte d'unicité : (sessionId, etudiantId) → RG16 (1 exercice par
 * étudiant et par session).
 *
 * Statuts possibles : EN_ATTENTE, EN_ATTENTE_SANS_RELECTEUR, RELUE.
 * (DEPOSE est un état transitoire interne, jamais persisté).
 */
@Entity
@Table(
    name = "exercices",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_exercice_session_etudiant",
        columnNames = {"session_id", "etudiant_id"}
    )
)
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private Long sessionId;

    @Column(name = "etudiant_id", nullable = false)
    private Long etudiantId;

    @Column(nullable = false, length = 500)
    private String lien;

    @Column(nullable = false, length = 30)
    private String statut;

    @Column(name = "relecteur_id")
    private Long relecteurId;

    @Column(name = "depose_at", nullable = false)
    private LocalDateTime deposeAt;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Long getRelecteurId() { return relecteurId; }
    public void setRelecteurId(Long relecteurId) { this.relecteurId = relecteurId; }

    public LocalDateTime getDeposeAt() { return deposeAt; }
    public void setDeposeAt(LocalDateTime deposeAt) { this.deposeAt = deposeAt; }
}
