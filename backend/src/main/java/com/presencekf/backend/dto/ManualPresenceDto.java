package com.presencekf.backend.dto;

import java.time.LocalDateTime;

public class ManualPresenceDto {
    private Long id;
    private String etudiant;
    private String code;
    private LocalDateTime timestamp;

    public ManualPresenceDto() {}
    public ManualPresenceDto(Long id, String etudiant, String code, LocalDateTime timestamp) {
        this.id = id;
        this.etudiant = etudiant;
        this.code = code;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
