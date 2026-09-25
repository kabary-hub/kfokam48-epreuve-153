package com.presencekf.backend.dto;

public class RelectureDto {
    private Long id;
    private Long exerciceId;
    private boolean terminee;

    public RelectureDto() {}
    public RelectureDto(Long id, Long exerciceId, boolean terminee) {
        this.id = id;
        this.exerciceId = exerciceId;
        this.terminee = terminee;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExerciceId() { return exerciceId; }
    public void setExerciceId(Long exerciceId) { this.exerciceId = exerciceId; }
    public boolean isTerminee() { return terminee; }
    public void setTerminee(boolean terminee) { this.terminee = terminee; }
}
