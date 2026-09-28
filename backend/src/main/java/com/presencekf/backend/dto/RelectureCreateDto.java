package com.presencekf.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO d'entrée pour POST /api/relectures/{id} (M6, EF7).
 * Corps imposé : {note, commentaire}.
 */
public class RelectureCreateDto {

    @NotNull(message = "La note est obligatoire")
    @Min(value = 0, message = "La note doit être entre 0 et 20")
    @Max(value = 20, message = "La note doit être entre 0 et 20")
    private Integer note;

    private String commentaire;

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
}
