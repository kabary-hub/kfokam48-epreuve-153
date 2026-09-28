package com.presencekf.backend.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO d'entrée pour POST /api/presences/formateur (M3, EF3, Q14).
 * Corps imposé : {sessionId, etudiantId}.
 *
 * La présence créée portera source = "FORMATEUR" (RG13).
 */
public class PresenceFormateurCreateDto {

    @NotNull(message = "sessionId est obligatoire")
    private Long sessionId;

    @NotNull(message = "etudiantId est obligatoire")
    private Long etudiantId;

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }
}
