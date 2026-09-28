package com.presencekf.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entité Relecture, mappée sur la table `relectures` de la migration V1.
 *
 * Champs : id, exerciceId, relecteurId, note, commentaire, statut, rendueAt.
 * La relation est 1-1 avec l'exercice (UNIQUE sur exercice_id).
 *
 * Statuts possibles : EN_ATTENTE, RELUE.
 * Note : entier 0-20 (RG8), nullable tant que non rendue.
 * Commentaire : immuable après la première soumission (RG9, Q10 > Q15).
 */
@Entity
@Table(name = "relectures")
public class Relecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercice_id", nullable = false, unique = true)
    private Long exerciceId;

    @Column(name = "relecteur_id", nullable = false)
    private Long relecteurId;

    @Column
    private Integer note;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(nullable = false, length = 20)
    private String statut;

    @Column(name = "rendue_at")
    private LocalDateTime rendueAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }

    public Long getRelecteurId() { return relecteurId; }
    public void setRelecteurId(Long relecteurId) { this.relecteurId = relecteurId; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getRendueAt() { return rendueAt; }
    public void setRendueAt(LocalDateTime rendueAt) { this.rendueAt = rendueAt; }
}
