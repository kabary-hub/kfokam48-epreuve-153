package com.presencekf.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;

    @Column(nullable = false)
    private boolean ouverte;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presence> presences = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercice> exercices = new ArrayList<>();

    public Session() {}

    public Session(String code, LocalDateTime debut, LocalDateTime fin) {
        this.code = code;
        this.debut = debut;
        this.fin = fin;
        this.ouvertes = false;
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

    public List<Presence> getPresences() { return presences; }
    public void setPresences(List<Presence> presences) { this.presences = presences; }

    public List<Exercice> getExercices() { return exercices; }
    public void setExercices(List<Exercice> exercices) { this.exercices = exercices; }

    public void ajouterPresence(Presence presence) {
        presences.add(presence);
        presence.setSession(this);
    }

    public void ajouterExercice(Exercice exercice) {
        exercices.add(exercice);
        exercice.setSession(this);
    }

    public void fermer() {
        this.ouverte = false;
    }
}
