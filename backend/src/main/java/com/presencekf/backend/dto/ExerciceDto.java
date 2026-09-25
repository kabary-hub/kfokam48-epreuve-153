package com.presencekf.backend.dto;

public class ExerciceDto {
    private Long id;
    private String lien;
    private String etudiant;

    public ExerciceDto() {}
    public ExerciceDto(Long id, String lien, String etudiant) {
        this.id = id;
        this.lien = lien;
        this.etudiant = etudiant;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }
    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }
}
