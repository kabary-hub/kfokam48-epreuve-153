package com.presencekf.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(name = "ouverture_at", nullable = false)
    private LocalDateTime ouvertureAt;

    @Column(name = "expiration_at", nullable = false)
    private LocalDateTime expirationAt;

    @Column(name = "cloture_at")
    private LocalDateTime clotureAt;

    @Column(name = "promotion_id", nullable = false)
    private Long promotionId;

    @Column(name = "formateur_id")
    private Long formateurId;

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public LocalDateTime getOuvertureAt() { return ouvertureAt; }
    public void setOuvertureAt(LocalDateTime ouvertureAt) { this.ouvertureAt = ouvertureAt; }

    public LocalDateTime getExpirationAt() { return expirationAt; }
    public void setExpirationAt(LocalDateTime expirationAt) { this.expirationAt = expirationAt; }

    public LocalDateTime getClotureAt() { return clotureAt; }
    public void setClotureAt(LocalDateTime clotureAt) { this.clotureAt = clotureAt; }

    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }

    public Long getFormateurId() { return formateurId; }
    public void setFormateurId(Long formateurId) { this.formateurId = formateurId; }
}
