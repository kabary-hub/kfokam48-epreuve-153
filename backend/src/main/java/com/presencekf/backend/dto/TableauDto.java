package com.presencekf.backend.dto;

import java.util.List;

public class TableauDto {
    private Long id;
    private String code;
    private boolean ouverte;
    private List<PresenceDto> presences;
    private List<ExerciceDto> exercices;

    public TableauDto() {}
    public TableauDto(Long id, String code, boolean ouverte, List<PresenceDto> presences, List<ExerciceDto> exercices) {
        this.id = id;
        this.code = code;
        this.ouverte = ouverte;
        this.presences = presences;
        this.exercices = exercices;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public boolean isOuverte() { return ouverte; }
    public void setOuverte(boolean ouverte) { this.ouverte = ouverte; }
    public List<PresenceDto> getPresences() { return presences; }
    public void setPresences(List<PresenceDto> presences) { this.presences = presences; }
    public List<ExerciceDto> getExercices() { return exercices; }
    public void setExercices(List<ExerciceDto> exercices) { this.exercices = exercices; }
}
