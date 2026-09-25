package com.presencekf.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "relecteur")
public class Relecteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercice_id", nullable = false)
    private Exercice exercice;

    public Relecteur() {}
    public Relecteur(String etudiant, Exercice exercice) {
        this.etudiant = etudiant;
        this.exercice = exercice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }
    public Exercice getExercice() { return exercice; }
    public void setExercice(Exercice exercice) { this.exercice = exercice; }
}
