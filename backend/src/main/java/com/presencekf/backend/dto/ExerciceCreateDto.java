package com.presencekf.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO d'entrée pour POST /api/exercices.
 * Corps imposé : {sessionId, etudiantId, lien}.
 */
public class ExerciceCreateDto {

    @NotNull(message = "sessionId est obligatoire")
    private Long sessionId;

    @NotNull(message = "etudiantId est obligatoire")
    private Long etudiantId;

    @NotBlank(message = "Le lien est obligatoire")
    @Size(max = 500, message = "Le lien ne doit pas dépasser 500 caractères")
    private String lien;

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }
}
