-- Migration V1 — Schéma initial PresenceKF
-- Conforme au diagramme D2 (docs/diagrammes/D2-modele-donnees.md)
-- Date : 2026-09-27

CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE etudiants (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    promotion_id BIGINT NOT NULL REFERENCES promotions(id)
);

CREATE TABLE formateurs (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL
);

CREATE TABLE sessions (
    id BIGSERIAL PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    code VARCHAR(6) NOT NULL,
    ouverture_at TIMESTAMP NOT NULL,
    expiration_at TIMESTAMP NOT NULL,
    cloture_at TIMESTAMP,
    promotion_id BIGINT NOT NULL REFERENCES promotions(id),
    formateur_id BIGINT REFERENCES formateurs(id)
);

CREATE TABLE presences (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id),
    etudiant_id BIGINT NOT NULL REFERENCES etudiants(id),
    source VARCHAR(20) NOT NULL CHECK (source IN ('ETUDIANT', 'FORMATEUR')),
    marque_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_presence_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE exercices (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES sessions(id),
    etudiant_id BIGINT NOT NULL REFERENCES etudiants(id),
    lien VARCHAR(500) NOT NULL,
    statut VARCHAR(30) NOT NULL,
    relecteur_id BIGINT REFERENCES etudiants(id),
    depose_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_exercice_session_etudiant UNIQUE (session_id, etudiant_id)
);

CREATE TABLE relectures (
    id BIGSERIAL PRIMARY KEY,
    exercice_id BIGINT NOT NULL UNIQUE REFERENCES exercices(id),
    relecteur_id BIGINT NOT NULL REFERENCES etudiants(id),
    note INT CHECK (note BETWEEN 0 AND 20),
    commentaire TEXT,
    statut VARCHAR(20) NOT NULL,
    rendue_at TIMESTAMP
);

-- Index pour les requêtes fréquentes
CREATE INDEX idx_sessions_code ON sessions(code);
CREATE INDEX idx_presences_session ON presences(session_id);
CREATE INDEX idx_exercices_session ON exercices(session_id);
CREATE INDEX idx_exercices_relecteur ON exercices(relecteur_id);
CREATE INDEX idx_relectures_relecteur ON relectures(relecteur_id);
