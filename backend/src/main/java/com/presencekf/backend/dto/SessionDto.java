package com.presencekf.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SessionDto {
    private Long id;
    private String code;
    private LocalDateTime debut;
    private LocalDateTime fin;
    private boolean ouverte;
    private List<PresenceDto> presences;
    private List<ExerciceDto> exercices;

    public SessionDto() {}
    public SessionDto(Long id, String code, LocalDateTime debut, LocalDateTime fin, boolean ouverte) {
        this.id = id;
        this.code = code;
        this.debut = debut;
        this.fin = fin;
        this.ouverte = ouverte;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public LocalDateTime getDebut() { return debut; }
    public void setDebut(LocalDateTime debut) { this.debut = debut; }
    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }
    public boolean isOuverte() { return ouverte; }
    public void setOuverte(boolean ouverte) { this.ouverte = ouverte; }
    public List<PresenceDto> getPresences() { return presences; }
    public void setPresences(List<PresenceDto> presences) { this.presences = presences; }
    public List<ExerciceDto> getExercices() { return exercices; }
    public void setExercices(List<ExerciceDto> exercices) { this.exercices = exercices; }
}
