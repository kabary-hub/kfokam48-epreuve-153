package com.presencekf.backend.dto;

/**
 * DTO représentant une ligne du tableau récapitulatif du formateur (M7, EF9).
 * Conforme au contrat : {etudiantId, nom, presences, exercicesDeposes,
 * moyenne, relecturesEnAttente}.
 *
 * La moyenne est calculée côté API (F3 du CDC). Si aucun exercice n'a été
 * relu, moyenne vaut null.
 */
public class TableauDto {

    private Long etudiantId;
    private String nom;
    private long presences;
    private long exercicesDeposes;
    private Double moyenne;
    private boolean provisoire;
    private long relecturesEnAttente;

    public TableauDto() {}

    public TableauDto(Long etudiantId, String nom, long presences,
                      long exercicesDeposes, Double moyenne,
                      boolean provisoire, long relecturesEnAttente) {
        this.etudiantId = etudiantId;
        this.nom = nom;
        this.presences = presences;
        this.exercicesDeposes = exercicesDeposes;
        this.moyenne = moyenne;
        this.provisoire = provisoire;
        this.relecturesEnAttente = relecturesEnAttente;
    }

    public Long getEtudiantId() { return etudiantId; }
    public void setEtudiantId(Long etudiantId) { this.etudiantId = etudiantId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public long getPresences() { return presences; }
    public void setPresences(long presences) { this.presences = presences; }

    public long getExercicesDeposes() { return exercicesDeposes; }
    public void setExercicesDeposes(long exercicesDeposes) { this.exercicesDeposes = exercicesDeposes; }

    public Double getMoyenne() { return moyenne; }
    public void setMoyenne(Double moyenne) { this.moyenne = moyenne; }

    public long getRelecturesEnAttente() { return relecturesEnAttente; }
    public void setRelecturesEnAttente(long relecturesEnAttente) {
        this.relecturesEnAttente = relecturesEnAttente;
    }

    public boolean isProvisoire() { return provisoire; }
    public void setProvisoire(boolean provisoire) { this.provisoire = provisoire; }
}
