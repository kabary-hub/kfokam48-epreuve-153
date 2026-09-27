-- Migration V2 — Données de démonstration PresenceKF
-- Permet au correcteur d'avoir une application non vide au démarrage.

INSERT INTO promotions (id, nom) VALUES
    (1, 'KFOKAM48 — Promotion 2026');

INSERT INTO formateurs (id, nom) VALUES
    (1, 'Formateur Principal');

INSERT INTO etudiants (id, nom, promotion_id) VALUES
    (1, 'Étudiant Démo 1', 1),
    (2, 'Étudiant Démo 2', 1),
    (3, 'Étudiant Démo 3', 1);

-- Réinitialiser les séquences après insertion explicite
SELECT setval('promotions_id_seq', 1, true);
SELECT setval('formateurs_id_seq', 1, true);
SELECT setval('etudiants_id_seq', 3, true);
