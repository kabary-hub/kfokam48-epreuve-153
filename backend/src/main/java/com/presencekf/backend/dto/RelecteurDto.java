package com.presencekf.backend.dto;

public class RelecteurDto {
    private Long id;
    private String etudiant;
    private Long exerciceId;

    public RelecteurDto() {}
    public RelecteurDto(Long id, String etudiant, Long exerciceId) {
        this.id = id;
        this.etudiant = etudiant;
        this.exerciceId = exerciceId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }
    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }
}
