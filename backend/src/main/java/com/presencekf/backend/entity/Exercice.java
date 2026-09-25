package com.presencekf.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exercice")
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String lien;

    @Column(nullable = false)
    private String etudiant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    public Exercice() {}

    public Exercice(String lien, String etudiant) {
        this.lien = lien;
        this.etudiant = etudiant;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }

    public String getEtudiant() { return etudiant; }
    public void setEtudiant(String etudiant) { this.etudiant = etudiant; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
}
