package com.presencekf.backend.dto;

public class RelectureModificationDto {
    private Long id;
    private String note;
    private String commentaire;

    public RelectureModificationDto() {}
    public RelectureModificationDto(Long id, String note, String commentaire) {
        this.id = id;
        this.note = note;
        this.commentaire = commentaire;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
