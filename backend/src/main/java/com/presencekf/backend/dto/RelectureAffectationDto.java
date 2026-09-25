package com.presencekf.backend.dto;

public class RelectureAffectationDto {
    private Long id;
    private String etudiant;
    private Long exerciceId;
    private String lienExercice;

    public RelectureAffectationDto() {}
    public RelectureAffectationDto(Long id, String etudiant, Long exerciceId, String lienExercice) {
        this.id = id;
        this.etudiant = etudiant;
        this.exerciceId = exerciceId;
        this.lienExercice = lienExercice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }
    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }
    public String getLienExercice() { return lienExercice; }
    public void setLienExercice(String lienExercice) { this.lienExercice = lienExercice; }
}
