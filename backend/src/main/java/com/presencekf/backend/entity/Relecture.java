package com.presencekf.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "relecture")
public class Relecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercice_id", nullable = false)
    private Exercice exercice;

    @Column(nullable = false)
    private boolean terminee;

    public Relecture() {}
    public Relecture(Exercice exercice) {
        this.exercice = exercice;
        this.terminee = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Exercice getExercice() { return exercice; }
    public void setExercice(Exercice exercice) { this.exercice = exercice; }
    public boolean isTerminee() { return terminee; }
    public void setTerminee(boolean terminee) { this.terminee = terminee; }
}
