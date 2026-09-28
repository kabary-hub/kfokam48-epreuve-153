package com.presencekf.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO d'entrée pour POST /api/presences.
 * Correspond au corps imposé par le contrat : {code, etudiantId}.
 *
 * Les validations déclenchent une erreur 400 {code, message} via
 * GlobalExceptionHandler (B4).
 */
public class PresenceCreateDto {

    @NotBlank(message = "Le code est obligatoire")
    @Size(min = 6, max = 6, message = "Le code doit faire 6 caractères")
    private String code;

    @NotNull(message = "etudiantId est obligatoire")
    private Long etudiantId;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }
}
