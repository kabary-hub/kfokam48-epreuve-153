package com.presencekf.backend.dto;

import java.time.LocalDateTime;

public class AutoClotureDto {
    private Long id;
    private String code;
    private boolean ouverte;
    private LocalDateTime fin;
    private boolean expirée;

    public AutoClotureDto() {}
    public AutoClotureDto(Long id, String code, boolean ouverte, LocalDateTime fin, boolean expirée) {
        this.id = id;
        this.code = code;
        this.ouverte = ouverte;
        this.fin = fin;
        this.expirée = expirée;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public boolean isOuverte() { return ouverte; }
    public void setOuverte(boolean ouverte) { this.ouverte = ouverte; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public boolean isExpirée() { return expirée; }
    public void setExpirée(boolean expirée) { this.expirée = expirée; }
}
