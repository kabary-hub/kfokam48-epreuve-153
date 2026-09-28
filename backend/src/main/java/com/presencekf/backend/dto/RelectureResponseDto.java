package com.presencekf.backend.dto;

/**
 * DTO de sortie pour POST /api/relectures/{id}.
 * Réponse 200 ; le contrat n'impose pas de schéma précis.
 * On renvoie {id, exerciceId, statut} pour traçabilité.
 */
public class RelectureResponseDto {

    private Long id;
    private Long exerciceId;
    private String statut;

    public RelectureResponseDto() {}

    public RelectureResponseDto(Long id, Long exerciceId, String statut) {
        this.id = id;
        this.exerciceId = exerciceId;
        this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
