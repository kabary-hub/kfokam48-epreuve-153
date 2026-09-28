package com.presencekf.backend.dto;

/**
 * DTO de sortie pour POST /api/exercices.
 * Réponse imposée : {id, statut}.
 */
public class ExerciceResponseDto {

    private Long id;
    private String statut;

    public ExerciceResponseDto() {}

    public ExerciceResponseDto(Long id, String statut) {
        this.id = id;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
