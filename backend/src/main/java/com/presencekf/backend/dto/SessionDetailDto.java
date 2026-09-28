package com.presencekf.backend.dto;

import java.time.LocalDateTime;

/**
 * DTO de sortie pour POST /api/sessions/{id}/cloture (M8, EF11).
 * Renvoie les détails de la session clôturée.
 */
public class SessionDetailDto {

    private Long id;
    private String titre;
    private String code;
    private LocalDateTime ouvertureAt;
    private LocalDateTime expirationAt;
    private LocalDateTime clotureAt;

    public SessionDetailDto() {}

    public SessionDetailDto(Long id, String titre, String code,
                            LocalDateTime ouvertureAt,
                            LocalDateTime expirationAt,
                            LocalDateTime clotureAt) {
        this.id = id;
        this.titre = titre;
        this.code = code;
        this.ouvertureAt = ouvertureAt;
        this.expirationAt = expirationAt;
        this.clotureAt = clotureAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getOuvertureAt() { return ouvertureAt; }
    public void setOuvertureAt(LocalDateTime ouvertureAt) { this.ouvertureAt = ouvertureAt; }

    public LocalDateTime getExpirationAt() { return expirationAt; }
    public void setExpirationAt(LocalDateTime expirationAt) { this.expirationAt = expirationAt; }

    public LocalDateTime getClotureAt() { return clotureAt; }
    public void setClotureAt(LocalDateTime clotureAt) { this.clotureAt = clotureAt; }
}
