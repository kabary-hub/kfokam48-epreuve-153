-- ============================================================
-- Migration V2 — Double relecture (issue #53, enveloppe étape 3)
-- ============================================================
-- Changement de besoin : chaque exercice est relu par 2 pairs
-- distincts. La note retenue est la moyenne des 2.
--
-- Modifications :
-- 1. Retirer la contrainte UNIQUE(exercice_id) sur relectures
-- 2. Ajouter UNIQUE(exercice_id, relecteur_id)
--
-- ⚠️ Ne JAMAIS modifier V1__init.sql. Cette migration est ADDITIVE.
-- ⚠️ Les données existantes doivent survivre :
--    - 1 relecture par exercice → reste valide (1 seule)
--    - Aucune colonne supprimée, aucune ligne supprimée.
--
-- V1__init.sql définit : exercice_id BIGINT NOT NULL UNIQUE REFERENCES exercices(id)
-- PostgreSQL génère automatiquement le nom de la contrainte : relectures_exercice_id_key
-- ============================================================

-- Étape 1 : supprimer la contrainte UNIQUE sur exercice_id
-- (nom autogénéré par PostgreSQL car inline dans V1)
ALTER TABLE relectures
    DROP CONSTRAINT IF EXISTS relectures_exercice_id_key;

-- Étape 2 : ajouter la nouvelle contrainte UNIQUE sur (exercice_id, relecteur_id)
ALTER TABLE relectures
    ADD CONSTRAINT uk_relecture_exercice_relecteur
    UNIQUE (exercice_id, relecteur_id);

-- Étape 3 : ajouter un index pour les requêtes par exercice (bonus performance)
CREATE INDEX IF NOT EXISTS idx_relectures_exercice
    ON relectures(exercice_id);

-- Étape 4 : ajouter relecteur2_id (nullable) sur exercices
-- Champ nullable : les exercices existants restent avec 1 seul relecteur
-- (statut PROVISOIRE après 1re relecture, RELUE après 2e)
ALTER TABLE exercices
    ADD COLUMN IF NOT EXISTS relecteur2_id BIGINT REFERENCES etudiants(id);

-- Étape 5 : index pour les requêtes par relecteur2 (bonus performance)
CREATE INDEX IF NOT EXISTS idx_exercices_relecteur2
    ON exercices(relecteur2_id);

-- Fin de la migration V2.
