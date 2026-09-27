package com.presencekf.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO d'entrée pour POST /api/sessions.
 * Correspond au corps imposé par le contrat : {titre, promotionId}.
 * Les validations déclenchent une erreur 400 {code, message} via
 * GlobalExceptionHandler (B4).
 */
public class SessionCreateDto {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String titre;

    @NotNull(message = "promotionId est obligatoire")
    private Long promotionId;

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }
}
