package com.presencekf.backend.dto;

/**
 * DTO de sortie pour POST /api/presences.
 * Correspond à la réponse imposée par le contrat :
 * {id, sessionId, etudiantId, source}.
 *
 * Aucune entité JPA n'est exposée directement (B3).
 */
public class PresenceResponseDto {

    private Long id;
    private Long sessionId;
    private Long etudiantId;
    private String source;

    public PresenceResponseDto() {
    }

    public PresenceResponseDto(Long id, Long sessionId, Long etudiantId, String source) {
        this.id = id;
        this.sessionId = sessionId;
        this.etudiantId = etudiantId;
        this.source = source;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
