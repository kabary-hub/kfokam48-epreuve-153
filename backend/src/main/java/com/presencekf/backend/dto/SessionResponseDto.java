package com.presencekf.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO de sortie pour POST /api/sessions.
 * Correspond à la réponse imposée par le contrat :
 * {id, code, ouvertureAt, expirationAt}.
 *
 * Aucune entité JPA n'est exposée directement (B3).
 */
public class SessionResponseDto {

    private Long id;
    private String code;
    private LocalDateTime ouvertureAt;
    private LocalDateTime expirationAt;

    public SessionResponseDto() {
    }

    public SessionResponseDto(Long id, String code,
                              LocalDateTime ouvertureAt,
                              LocalDateTime expirationAt) {
        this.id = id;
        this.code = code;
        this.ouvertureAt = ouvertureAt;
        this.expirationAt = expirationAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getOuvertureAt() { return ouvertureAt; }
    public void setOuvertureAt(LocalDateTime ouvertureAt) { this.ouvertureAt = ouvertureAt; }

    public LocalDateTime getExpirationAt() { return expirationAt; }
    public void setExpirationAt(LocalDateTime expirationAt) { this.expirationAt = expirationAt; }
}
